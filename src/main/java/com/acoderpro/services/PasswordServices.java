package com.acoderpro.services;

import com.acoderpro.dto.ForgotPasswordDTO;
import com.acoderpro.dto.VerifyOTPDTO;
import com.acoderpro.dto.ResetPasswordDTO;

public interface PasswordServices {
	
	public Integer requestOTP(ForgotPasswordDTO forgotPassword);
	
	public boolean otpVerification(VerifyOTPDTO verification);
	
	public boolean resetPassword(ResetPasswordDTO password);

}
