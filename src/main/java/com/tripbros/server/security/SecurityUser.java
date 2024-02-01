package com.tripbros.server.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SecurityUser extends User {

	private com.tripbros.server.user.domain.User user;

	public SecurityUser(com.tripbros.server.user.domain.User user) {
		super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;

	}
}
