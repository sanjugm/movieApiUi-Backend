package com.movieflix.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.movieflix.dto.MailBody;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;

	public void sendSimpleMessage(MailBody mailBody) throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("sanjugm2344@gmail.com");
		message.setTo(mailBody.getTo());
		message.setSubject(mailBody.getSubject());
		message.setText(mailBody.getText());

		javaMailSender.send(message);
	}
}
