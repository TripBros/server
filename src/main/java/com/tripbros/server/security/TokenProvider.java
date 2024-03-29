package com.tripbros.server.security;

import static com.tripbros.server.security.JwtFilter.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

	protected long tokenValidityInMilliseconds;
	protected Key key;
	private final UserDetailsServiceImpl detailsService;

	@Autowired
	public TokenProvider(@Value("${jwt.secret_key}") String secret, @Value("${jwt.expiration_time}")  long tokenValidityInMilliseconds, UserDetailsServiceImpl detailsService){
		this.detailsService = detailsService;
		//시크릿 키 저장소
		this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
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

	public JwtDTO createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));



		Date validity = new Date(new Date().getTime() + this.tokenValidityInMilliseconds);

		String accessToken = Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(this.key, SignatureAlgorithm.HS256)
			.setExpiration(validity)
			.compact();
		return JwtDTO.builder().grantType("Bearer").accessToken(accessToken).build();
	}

	public String extractJwtFromStomp(final StompHeaderAccessor accessor) {

		String bearer = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
			return bearer.substring(7);
		}
		return null;
	}





}
