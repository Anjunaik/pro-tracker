package com.acoderpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acoderpro.dto.ForgotPasswordDTO;
import com.acoderpro.services.PasswordServices;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {

	@Autowired
	private PasswordServices passwordServices;

	@PostMapping("/forgot-pass")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {

		return ResponseEntity.ok(passwordServices.requestOTP(dto));

	}

}
