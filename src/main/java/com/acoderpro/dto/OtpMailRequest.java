package com.acoderpro.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OtpMailRequest {

	private String to;
	private String subject;
	private String body;
}
