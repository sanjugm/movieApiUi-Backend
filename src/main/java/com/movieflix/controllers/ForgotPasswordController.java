package com.movieflix.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieflix.auth.entities.ForgotPassword;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.repositories.ForgotPasswordRepository;
import com.movieflix.auth.repositories.UserRepository;
import com.movieflix.auth.utils.ChangePassword;
import com.movieflix.dto.MailBody;
import com.movieflix.service.EmailService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/forgotPassword")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

	private final UserRepository userRepository;
	private final EmailService emailService;
	private final ForgotPasswordRepository forgotPasswordRepository;
	private final PasswordEncoder passwordEncoder;

	public ForgotPasswordController(UserRepository userRepository, EmailService emailService,
			ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.forgotPasswordRepository = forgotPasswordRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// ✅ Send OTP to email
	@PostMapping("/verifyMail/{email}")
	@Transactional
	public ResponseEntity<String> verifyEmail(@PathVariable String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid email: " + email));

		int otp = otpGenerator();
		MailBody mailBody = MailBody.builder().to(email).subject("OTP for Forgot Password request")
				.text("This is the OTP for your Forgot Password request: " + otp).build();
		ForgotPassword fp = ForgotPassword.builder().otp(otp)
				.expirationTime(new Date(System.currentTimeMillis() + 2 * 60 * 1000)).user(user).build();

		forgotPasswordRepository.save(fp);
		try {
			emailService.sendSimpleMessage(mailBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("OTP sent successfully to: " + email);

	}

	// ✅ Verify OTP
	@PostMapping("/verifyOtp/{otp}/{email}")
	public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid email: " + email));

		ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
				.orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

		if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
			forgotPasswordRepository.deleteById(fp.getId());
			return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
		}

		return ResponseEntity.ok("OTP verified!");
	}

	// ✅ Change password
	@PostMapping("/changePassword/{email}")
	public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
			@PathVariable String email) {
		if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
			return new ResponseEntity<>("Passwords do not match!", HttpStatus.EXPECTATION_FAILED);
		}

		String encodedPassword = passwordEncoder.encode(changePassword.password());
		userRepository.updatePassword(email, encodedPassword);

		return ResponseEntity.ok("Password has been changed!");
	}

	// Generate 6-digit OTP
	private Integer otpGenerator() {
		Random random = new Random();
		return random.nextInt(100_000, 999_999);
	}
}
