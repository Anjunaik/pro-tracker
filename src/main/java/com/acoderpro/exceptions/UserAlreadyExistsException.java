package com.acoderpro.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3324540995605343819L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
