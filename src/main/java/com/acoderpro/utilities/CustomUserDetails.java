package com.acoderpro.utilities;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.acoderpro.pojo.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UserEntity user;

	public CustomUserDetails(UserEntity entity) {
		this.user = entity;
	}

	public UUID getId() {
		return user.getId();
	}

	public String getEmail() {
		log.info(getEmail());
		return user.getEmail();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isActive();
	}

}
