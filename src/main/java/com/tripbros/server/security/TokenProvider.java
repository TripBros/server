package com.tripbros.server.security;

import static com.tripbros.server.security.JwtFilter.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tripbros.server.user.domain.RefreshToken;
import com.tripbros.server.user.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {

	protected static final String AUTHORITIES_KEY = "auth";

	protected long accessTokenValidityInMilliseconds;
	protected long refreshTokenValidityInMilliseconds;
	protected Key key;
	private final UserDetailsServiceImpl detailsService;
	private final RefreshTokenRepository refreshTokenRepository;

	@Autowired
	public TokenProvider(@Value("${jwt.secret_key}") String secret, @Value("${jwt.access_token_expiration_time}") long accessTokenValidityInMilliseconds,
		@Value("${jwt.refresh_token_expiration_time}") long refreshTokenValidityInMilliseconds, UserDetailsServiceImpl detailsService,
		RefreshTokenRepository refreshTokenRepository){
		this.detailsService = detailsService;
		this.refreshTokenRepository = refreshTokenRepository;
		//시크릿 키 저장소
		this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
		this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
		byte[] keyBytes = Decoders.BASE64.decode(secret); // base64 시크릿 키 디코딩
		key = Keys.hmacShaKeyFor(keyBytes); // HMAC 암호화
	}

	public Authentication getAuthentication(String token){
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		if (claims.get(AUTHORITIES_KEY) == null) {
			throw new RuntimeException("auth 정보가 없습니다.");
		}
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();

		UserDetails principal = detailsService.loadUserByUsername(claims.getSubject()); //todo : 무엇으로 바인딩 할지 결정
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);

	}

	public String getUserEmailFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("sub", String.class);
	}
	public boolean validateToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			return true;
		} catch (SecurityException | MalformedJwtException e){
			log.info("잘못된 JWT 서명");
			throw new UnauthorizedAccessException(SecurityExceptionMessage.MalformedJwt.getMessage());
		} catch (ExpiredJwtException e){
			log.info("만료된 JWT");
			throw new UnauthorizedAccessException(SecurityExceptionMessage.ExpiredJwt.getMessage());
		} catch (UnsupportedJwtException e){
			log.info("지원하지 않는 JWT");
			throw new UnauthorizedAccessException(SecurityExceptionMessage.UnsupportedJwt.getMessage());
		} catch (IllegalArgumentException e){
			log.info("잘못된 JWT");
		}
		return false;
	}

	public JwtDTO createTokens(Authentication authentication) {
		String accessToken = createAccessToken(authentication);
		String refreshToken = createRefreshToken(accessToken);

		return JwtDTO.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).build();
	}

	private String createAccessToken(Authentication authentication){
		String authorities = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		Date accessValidity = new Date(new Date().getTime() + this.accessTokenValidityInMilliseconds);
		return Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(this.key, SignatureAlgorithm.HS256)
			.setExpiration(accessValidity)
			.compact();
	}

	private String createRefreshToken(String accessToken){
		Date refreshValidity = new Date(new Date().getTime() + this.refreshTokenValidityInMilliseconds);
		String refreshToken = Jwts.builder()
			.signWith(this.key, SignatureAlgorithm.HS256)
			.setExpiration(refreshValidity)
			.compact();

		// RefreshToken DB에 저장
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		SecurityUser user = (SecurityUser) detailsService.loadUserByUsername(claims.getSubject());
		refreshTokenRepository.save(new RefreshToken(user.getUser(), refreshToken, refreshValidity));

		return refreshToken;
	}

	public String validateRefreshToken(String accessToken, String token){
		RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
			.orElseThrow(() -> new UnauthorizedAccessException("존재하지 않는 refresh token"));
		String subject;
		// 만료된 accessToken의 주인과 입력한 refresh token의 유저가 같은지 판단
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
			subject = claims.getSubject();
		} catch (ExpiredJwtException e){
			subject = e.getClaims().getSubject();
		}
		if(!subject.equals(refreshToken.getUser().getEmail()))
			throw new UnauthorizedAccessException("만료된 access token의 user와 요청한 refresh token의 user가 동일하지 않습니다.");

		// refresh token이 유효하지 않은 경우 (만료기간이 지난 경우) => 로그인 풀도록 요청
		if(refreshToken.getValidity().before(new Date())) {
			// RefreshToken DB에서 해당 엔티티 삭제 후 만료 response 반환
			refreshTokenRepository.delete(refreshToken);
			return HttpStatus.UNAUTHORIZED.toString();
		}
		// refresh token이 유효한 경우 => access token 발급
		else{
			UserDetails userDetails = detailsService.loadUserByUsername(refreshToken.getUser().getEmail());
			Authentication authentication = new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(), null, userDetails.getAuthorities());
			return createAccessToken(authentication);
		}
	}

	public String extractJwtFromStomp(final StompHeaderAccessor accessor) {
		String bearer = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
			return bearer.substring(7);
		}
		return null;
	}

}
