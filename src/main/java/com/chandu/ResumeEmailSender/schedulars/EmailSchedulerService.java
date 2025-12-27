package com.chandu.ResumeEmailSender.schedulars;

import com.chandu.ResumeEmailSender.model.HrDetails;
import com.chandu.ResumeEmailSender.service.EmailService;
import com.chandu.ResumeEmailSender.service.ExcelReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmailSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSchedulerService.class);

    @Autowired
    private ExcelReaderService excelReaderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${resume.file.path}")
    private String resumePath;

    private List<HrDetails> hrDetailsList;
    private AtomicInteger currentIndex = new AtomicInteger(0);
    private boolean initialized = false;

    public void initialize() {
        if (!initialized) {
            hrDetailsList = excelReaderService.readHrDetails();

            if (hrDetailsList.isEmpty()) {
                logger.warn("No HR details found. Stopping.");
                shutdownApplication();
                return;
            }

            logger.info("Loaded {} HR entries", hrDetailsList.size());
            initialized = true;
        }
    }

    @Scheduled(fixedDelayString = "${email.send.interval:5000}")
    public void sendNextEmail() {

        if (!initialized)
            initialize();

        if (currentIndex.get() >= hrDetailsList.size()) {
            logger.info("All emails processed.");
            shutdownApplication();
            return;
        }

        HrDetails hr = hrDetailsList.get(currentIndex.getAndIncrement());

        if (!isEligible(hr)) {
            logger.info("Skipped → {} | Loc={} | Exp={}",
                    hr.getCompanyName(), hr.getLocation(), hr.getExperience());
            return;
        }

        sendEmailToHr(hr);
    }

    private boolean isEligible(HrDetails hr) {
        // All rows are considered eligible as per user request
        return true;
    }

    private void sendEmailToHr(HrDetails hr) {

        String email = hr.getHrEmail();
        if (email == null || !email.contains("@"))
            return;

        String subject = "Application for Java Backend Developer – " + hr.getCompanyName() + " | Chandu Raparthi";

        String salutation = (hr.getHrName() != null && !hr.getHrName().isEmpty()) ? "Dear " + hr.getHrName() + ","
                : "Dear Hiring Team,";

        String body = salutation + "\n\n" +
                "I hope you are doing well.\n\n" +
                "My name is Chandu Raparthi, and I am a Java Backend Developer with 3 years of experience " +
                "working with Java, Spring Boot, Microservices, REST APIs, and SQL.\n\n" +

                "I am reaching out regarding backend opportunities at " + hr.getCompanyName() + ".\n\n" +

                "Currently, I am working at Cybrowse Digital Solutions Pvt. Ltd., Hyderabad, " +
                "where I work as a Junior Java Backend Developer on backend application development.\n\n" +

                "📌 Preferred Location: Hyderabad or Remote\n" +
                "🕒 Notice Period: 9 days\n\n" +

                "Please find my resume attached for your review.\n\n" +

                "Warm regards,\n" +
                "Chandu Raparthi\n" +
                "+91 9452301058\n" +
                "raaparthichandu@gmail.com\n";

        try {
            emailService.sendEmailWithAttachment(email, subject, body, resumePath);

        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage());
        }
    }

    private void shutdownApplication() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.exit(SpringApplication.exit(applicationContext));
            } catch (Exception ignored) {
            }
        }).start();
    }
}
