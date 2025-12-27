package com.chandu.ResumeEmailSender;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ResumeEmailSenderApplicationTests {

	@Test
	void contextLoads() {
		assertEquals(true,true);
	}

	
	@Autowired
	private JavaMailSender mailSender;

	public void testEmail() {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo("your.email@example.com");
	    message.setSubject("Test Email");
	    message.setText("This is a test email from Spring Boot!");
	    mailSender.send(message);
	}

	
}
