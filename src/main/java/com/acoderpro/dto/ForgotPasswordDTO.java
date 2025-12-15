package com.acoderpro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ForgotPasswordDTO {
	
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	@Size(max = 50, min = 7, message = "Mail must be between 7 and 50 characters")
	private String email;

}
