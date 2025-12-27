package com.chandu.ResumeEmailSender.controller;

import com.chandu.ResumeEmailSender.model.HrDetails;
import com.chandu.ResumeEmailSender.schedulars.EmailSchedulerService;
import com.chandu.ResumeEmailSender.service.ExcelReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResumeMailerController {

    @Autowired
    private ExcelReaderService excelReaderService;

    @Autowired
    private EmailSchedulerService emailSchedulerService;

    @GetMapping("/send-emails")
    public String sendEmails() {
        // Initialize the scheduler service
        emailSchedulerService.initialize();

        // Preview the data that will be processed
        List<HrDetails> hrDetailsList = excelReaderService.readHrDetails();

        if (hrDetailsList.isEmpty()) {
            return "No HR details found in the Excel file.";
        }

        return "Email sending process has been initialized. " +
                hrDetailsList.size() + " emails will be sent sequentially. " +
                "Check application logs for progress.";
    }

    @GetMapping("/preview-data")
    public List<HrDetails> previewData() {
        return excelReaderService.readHrDetails();
    }
}