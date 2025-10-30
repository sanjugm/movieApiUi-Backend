package com.movieflix.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieflix.dto.MailBody;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

	private final EmailService emailService;

	public TestController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/send-email")
	public ResponseEntity<String> sendEmail(@RequestBody MailBody mailBody) {
		try {
			emailService.sendSimpleMessage(mailBody);
			return ResponseEntity.ok("Email sent successfully! ✅");
		} catch (Exception e) {
			e.printStackTrace(); // for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to send email: " + e.getMessage());
		}
	}

}
