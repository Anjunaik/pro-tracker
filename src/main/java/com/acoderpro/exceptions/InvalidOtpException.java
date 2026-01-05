package com.acoderpro.exceptions;

public class InvalidOtpException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8527965154352506800L;

	public InvalidOtpException(String message) {
		super(message);
	}
}
