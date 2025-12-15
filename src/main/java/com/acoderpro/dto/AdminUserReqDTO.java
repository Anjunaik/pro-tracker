package com.acoderpro.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserReqDTO {

	@Size(max = 50, min = 3, message = "Frist name must be between 3 and 50 characters")
	private String firstName;

	@Size(max = 50, min = 3, message = "Last name must be between 3 and 50 characters")
	private String lastName;

	private String middleName;

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	@Size(max = 50, min = 7, message = "Mail must be between 7 and 50 characters")
	private String email;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "Password must be 8-20 characters and include upper, lower, number, and special character")
	private String password;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "Password must be 8-20 characters and include upper, lower, number, and special character")
	private String confirmPassword;

	private Set<Integer> roles = new HashSet<Integer>();
}
