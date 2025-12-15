package com.acoderpro.servicesImp;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.acoderpro.dto.ForgotPasswordDTO;
import com.acoderpro.dto.ResetPasswordDTO;
import com.acoderpro.dto.VerifyOTPDTO;
import com.acoderpro.pojo.PasswordResetToken;
import com.acoderpro.pojo.UserEntity;
import com.acoderpro.repository.UserRepository;
import com.acoderpro.services.PasswordServices;

@Service
public class PasswordServiceImp implements PasswordServices {

	private UserRepository userRepo;

	public PasswordServiceImp(@Autowired UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public Integer requestOTP(ForgotPasswordDTO dto) {

		//Check whether user exists or not
		UserEntity user = userRepo.findByEmail(dto.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("No, User found with this user name"));

		//OTP generation
		String otp = String.valueOf(new Random().nextInt(900000) + 100000);
		
		//Storing OTP details in DB with some expire time
		  PasswordResetToken token = new PasswordResetToken();
		    token.setEmail(dto.getEmail());
		    token.setOtp(otp);
		    token.setExpiryTime(LocalDateTime.now().plusMinutes(5));
		    token.setUsed(false);
		    
		return 1;
	}

	@Override
	public boolean otpVerification(VerifyOTPDTO verification) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resetPassword(ResetPasswordDTO password) {
		// TODO Auto-generated method stub
		return false;
	}

}
