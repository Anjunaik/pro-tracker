package com.acoderpro.servicesImp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acoderpro.dto.ChangePasswordDTO;
import com.acoderpro.dto.ForgotPasswordDTO;
import com.acoderpro.dto.ResetPasswordDTO;
import com.acoderpro.dto.VerifyOTPDTO;
import com.acoderpro.exceptions.AccountInactiveException;
import com.acoderpro.exceptions.InvalidOtpException;
import com.acoderpro.pojo.PasswordResetToken;
import com.acoderpro.pojo.UserEntity;
import com.acoderpro.repository.PasswordReset;
import com.acoderpro.repository.UserRepository;
import com.acoderpro.services.PasswordServices;
import com.acoderpro.utilities.EmailTemplates;
import com.acoderpro.utilities.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PasswordServiceImp implements PasswordServices {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordReset reset;

	@Autowired
	private PasswordEncoder encode;

	@Autowired
	private JWTUtil getUsername;

	private final NotificationClientImp clientImp;

	public PasswordServiceImp(UserRepository userRepo, NotificationClientImp clientImp) {
		this.userRepo = userRepo;
		this.clientImp = clientImp;
	}

	@Override
	public String requestOTP(ForgotPasswordDTO dto) {

		// 1️⃣ Validate user
		UserEntity user = userRepo.findByEmail(dto.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("No user found with this email"));
		if (!user.isActive()) {
			clientImp.sendOtp(EmailTemplates.createMailContent("ProTrack - Password Reset OTP", user.getEmail(),
					EmailTemplates.accountInactiveTemplate(user.getFirstName())));
			throw new AccountInactiveException("Your account is inactive.Please check mail and contact support.");
		}

		// 2️⃣ Generate OTP
		String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));

		// 3️⃣ Save OTP
		reset.deleteByEmail(dto.getEmail());

		PasswordResetToken token = new PasswordResetToken();
		token.setEmail(dto.getEmail());
		token.setOtp(encode.encode(otp));
		token.setExpiryTime(LocalDateTime.now().plusMinutes(10));
		token.setUsed(false);
		reset.save(token);

		// 4️⃣ Send OTP mail (FAILS request if notification is down)

		clientImp.sendOtp(EmailTemplates.createMailContent("ProTrack - Password Reset OTP", user.getEmail(),
				EmailTemplates.otpTemplate(otp)));

		return "OTP sent successfully";
	}

	@Override
	public String otpVerification(VerifyOTPDTO verification) {
		log.info("Otp verify method called");
		PasswordResetToken resetToken = reset.findByEmail(verification.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("No user found with this email"));

		if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new InvalidOtpException("OTP expired");
		}

		if (!encode.matches(verification.getOtp(), resetToken.getOtp())) {
			throw new InvalidOtpException("Invalid OTP");
		}

		resetToken.setUsed(true);
		reset.save(resetToken);
		log.info("otp verification result saved in DB");
		return "OTP verification success";
	}

	@Override
	public String resetPassword(ResetPasswordDTO password) {

		UserEntity userEntity = userRepo.findByEmail(password.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("Resource Not Found"));

		PasswordResetToken passwordToken = reset.findByEmail(password.getEmail())
				.orElseThrow(() -> new InvalidOtpException("OTP expired"));

		if (passwordToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new InvalidOtpException("OTP expired");
		}

		if (!passwordToken.isUsed())
			throw new InvalidOtpException("OTP Not Yet Verified");

		userEntity.setPassword(encode.encode(password.getNewPassword()));
		userEntity.setUserUpdated(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		userRepo.save(userEntity);
		reset.delete(passwordToken);

		clientImp.sendOtp(EmailTemplates.createMailContent("ProTrack - Password Reset Success", userEntity.getEmail(),
				EmailTemplates.passwordUpdateTemplate(userEntity.getFirstName())));
		return "Password Updated successfully";
	}

	// Password change
	public String changePassword(ChangePasswordDTO changePassword) {

		log.info("Password change method called");
		UserEntity userDetails = userRepo.findByEmail(getUsername.getCurrentUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Resource Not Found"));
		if (!encode.matches(changePassword.getOldPassword(), userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid old password");
		}

		// Encode and update new password
		userDetails.setPassword(encode.encode(changePassword.getNewPassword()));

		userRepo.save(userDetails);
		log.info("Password saved in DB");
		return "Password changed successfully";
	}

	@Transactional
	@Scheduled(fixedRate = 300000) // every 5 minutes
	public void deleteExpiredOtps() {
		log.info("Running OTP cleanup job");
		reset.deleteExpired(LocalDateTime.now());
	}
}
