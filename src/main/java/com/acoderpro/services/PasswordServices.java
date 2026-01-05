package com.acoderpro.services;

import com.acoderpro.dto.ChangePasswordDTO;
import com.acoderpro.dto.ForgotPasswordDTO;
import com.acoderpro.dto.ResetPasswordDTO;
import com.acoderpro.dto.VerifyOTPDTO;

public interface PasswordServices {

	public String requestOTP(ForgotPasswordDTO dto);

	public String otpVerification(VerifyOTPDTO verification);

	public String resetPassword(ResetPasswordDTO password);

	public String changePassword(ChangePasswordDTO changePassword);

}
