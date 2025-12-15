package com.acoderpro.globalexceptionhandling;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.acoderpro.exceptions.ConfirmPasswordMismatchException;
import com.acoderpro.exceptions.RoleNotFoundException;
import com.acoderpro.exceptions.UserAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;



@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandling {
	
	@ExceptionHandler(value = UserAlreadyExistsException.class)
	public ResponseEntity<?> userAlreadyExisted(UserAlreadyExistsException ex)
	{
		
		 Map<String, Object> error = new HashMap<>();
	        error.put("timestamp", LocalDateTime.now());
	        error.put("status", HttpStatus.CONFLICT.value());
	        error.put("error", "CONFLICT");
	        error.put("message", ex.getMessage());

	        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	        }
	
	@ExceptionHandler(value = RoleNotFoundException.class)
	public ResponseEntity<?> roleNotFound(RoleNotFoundException role)
	{
		 Map<String, Object> error = new HashMap<>();
	        error.put("timestamp", LocalDateTime.now());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        error.put("error", "BADREQUEST");
	        error.put("message", role.getMessage());

	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	        }
	
	
	@ExceptionHandler(value=ConfirmPasswordMismatchException.class)		
	public ResponseEntity<?> confirmPasswordMismatchException(ConfirmPasswordMismatchException ex)
	{

		 Map<String, Object> error = new HashMap<>();
	        error.put("timestamp", LocalDateTime.now());
	        error.put("status", HttpStatus.BAD_REQUEST.value());
	        error.put("error", "BADREQUEST");
	        error.put("message", ex.getMessage());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
