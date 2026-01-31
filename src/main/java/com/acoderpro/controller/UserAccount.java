package com.acoderpro.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.acoderpro.dto.AdminUserReqDTO;
import com.acoderpro.dto.ChangePasswordDTO;
import com.acoderpro.dto.DefaultUserReqDTO;
import com.acoderpro.dto.ResetPasswordDTO;
import com.acoderpro.dto.VerifyOTPDTO;
import com.acoderpro.pojo.UserEntity;
import com.acoderpro.services.PasswordServices;
import com.acoderpro.services.UserService;
import com.acoderpro.utilities.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1/account")
public class UserAccount {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordServices passwordServices;

	@Autowired
	private JWTUtil jwtUtil;

	/**
	 * @param defaultUserReqDTO
	 * @return number
	 * @throws if user already exists it throw user exists and return 409 code other
	 *            wise create user return the success message
	 */
	@PostMapping(value = "/create-user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> createAccount(@RequestBody DefaultUserReqDTO defaultUserReqDTO) {

		UserEntity userAccount = userService.createUserAccount(defaultUserReqDTO, false);

		return ResponseEntity.created(URI.create("/api/v1/account/users/" + userAccount.getId()))
				.body(Map.of("message", "User created successfully", "userId", "" + userAccount.getId()));

	}

	/**
	 * @param userEntity
	 * @return success code
	 * @throws if role not found and user already exists in data base throws
	 *            exception with meaningful message otherwise create user return
	 *            success message
	 */
	@PostMapping(value = "/admin-create-user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> createAccountByAdmin(@RequestBody AdminUserReqDTO adminUser) {
		// ystem.out.println(jwtUtil.isAdminLogged() + "check");
		userService.createUserAccount(adminUser, jwtUtil.isAdminLogged());
		return ResponseEntity.ok(userService.createUserAccount(adminUser, jwtUtil.isAdminLogged()));
	}

	@PostMapping("/otp-verify")
	public ResponseEntity<?> otpVerification(@RequestBody VerifyOTPDTO otp) {

		return ResponseEntity.ok(passwordServices.otpVerification(otp));
	}

	@PatchMapping(value = "/reset-pass", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO password) {

		return ResponseEntity.ok(passwordServices.resetPassword(password));
	}

	@PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePassword) {
		return ResponseEntity.ok(passwordServices.changePassword(changePassword));
	}

	@GetMapping("/user/{id}")
	@Operation(summary = "Get user by ID", description = "Provide a UUID to fetch a specific user")
	public ResponseEntity<?> getUser(
			@Parameter(description = "UUID of the user to fetch", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable("id") UUID id // <--
																																				// Explicitly
																																				// specify
																																				// name
																																				// here
	) {
		return ResponseEntity.ok(userService.findUserByID(id));
	}

	@PostMapping(value="/users-delete",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUserByAdmin(@RequestBody List<UUID> ids) {

		return ResponseEntity.ok(userService.deleteUserById(ids));
	}

	@GetMapping("/greet")
	public ResponseEntity<?> welcome() {
		return ResponseEntity.ok("Welcome");
	}

}
