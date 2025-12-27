package com.chandu.ResumeEmailSender.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {

		if (to == null || !to.contains("@")) {
			logger.warn("Invalid email : {}", to);
			return;
		}

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body);

			File file = new File(attachmentPath);
			if (!file.exists()) {
				throw new RuntimeException("Attachment not found: " + attachmentPath);
			}

			helper.addAttachment(file.getName(), file);

			mailSender.send(message);
			logger.info("📩 Email sent to {}", to);

		} catch (MessagingException e) {
			logger.error("Email sending failed to {}: {}", to, e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
