package com.acoderpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acoderpro.dto.JWTResponseDTO;
import com.acoderpro.dto.LoginDTO;
import com.acoderpro.utilities.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO request) {
		
		log.info("Login attempt for email: {}", request.getUsername());
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		log.info("after authentication");
		String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(request.getUsername()));

		log.info("token generated successfully");
		return ResponseEntity.ok(new JWTResponseDTO(token));
	}
}
