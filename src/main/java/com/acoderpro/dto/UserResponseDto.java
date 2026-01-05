package com.acoderpro.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponseDto {
	    private UUID id;
	    private String fullName;
	    private String email;
	    private List<String> roles;
	    private Date createdAt;

	    // constructors, getters, setters
	}


