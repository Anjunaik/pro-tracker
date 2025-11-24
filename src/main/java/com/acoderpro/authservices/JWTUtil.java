package com.acoderpro.authservices;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String generateToken(UserDetails userDetails) {
	
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
				.signWith(SECRET_KEY).compact();
	}

	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername());
	}

	public boolean isAdminLogged() {
		
		System.out.println("method called");
		@Nullable
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			

			System.out.println(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList()).contains("ROLE_ADMIN"));
			return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList()).contains("ROLE_ADMIN");
			

		}
		return false;
	}
}
