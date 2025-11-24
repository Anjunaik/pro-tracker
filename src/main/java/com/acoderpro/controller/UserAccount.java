package com.acoderpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acoderpro.authservices.JWTUtil;
import com.acoderpro.dto.AdminUserReqDTO;
import com.acoderpro.dto.DefaultUserReqDTO;
import com.acoderpro.services.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserAccount {

	@Autowired
	private UserService userService;

	@Autowired
	private JWTUtil jwtUtil;

	/**
	 * @param defaultUserReqDTO
	 * @return number
	 * @throws if user already exists it throw user exists and return 409 code other
	 *            wise create user return the success message
	 */
	@PostMapping(value = "/register/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Integer> createAccount(@RequestBody DefaultUserReqDTO defaultUserReqDTO) {

		userService.createUserAccount(defaultUserReqDTO, false);
		return ResponseEntity.ok(1);
	}

	/**
	 * @param userEntity
	 * @return success code
	 * @throws if role not found and user already exists in data base throws
	 *            exception with meaningful message otherwise create user return
	 *            success message
	 */
	@PostMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Integer> createAccountByAdmin(@RequestBody AdminUserReqDTO adminUser) {
		//ystem.out.println(jwtUtil.isAdminLogged() + "check");
		userService.createUserAccount(adminUser, jwtUtil.isAdminLogged());
		return ResponseEntity.ok(1);
	}
}
