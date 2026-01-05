package com.acoderpro.utilities;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.acoderpro.authconfiguration.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Use getServletPath() for accurate path matching
		log.error("Dofilter method is called");
		String path = request.getServletPath();
		System.out.println("Request path: " + path);

		// Skip public URLs
		if (Arrays.stream(SecurityConstants.PUBLIC_URLS).anyMatch(path::startsWith)) {
			log.warn("Skip security constants called" + path);
			chain.doFilter(request, response);
			return;
		}

		// Extract JWT token
		String token = extractJwtFromHeader(request);
		if (token == null) {
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization token is missing");
			return;
		}

		try {
			String username = jwtUtil.extractUsername(token);
			log.error("extract method called" + username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtUtil.validateToken(token, userDetails)) {
					// SecurityContextHolder.getContext().setAuthentication(token);

					log.error("AUTH BEFORE SET => {}", SecurityContextHolder.getContext().getAuthentication());
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);

					log.error("AUTH AFTER SET => {}", SecurityContextHolder.getContext().getAuthentication());
				} else {
					sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid or expired token");
					return;
				}
			}

		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid token: " + e.getMessage());
			return;
		}

		chain.doFilter(request, response);
	}

	private String extractJwtFromHeader(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		log.error("Authorization header: {}", authHeader);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Map<String, Object> errorBody = Map.of("status", status, "error", message, "timestamp",
				System.currentTimeMillis());
		objectMapper.writeValue(response.getWriter(), errorBody);
	}
}
