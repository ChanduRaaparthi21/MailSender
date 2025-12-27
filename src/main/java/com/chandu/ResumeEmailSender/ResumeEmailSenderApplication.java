package com.chandu.ResumeEmailSender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enable scheduling functionality
@EnableAsync // Enable async processing
public class ResumeEmailSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeEmailSenderApplication.class, args);
	}
}