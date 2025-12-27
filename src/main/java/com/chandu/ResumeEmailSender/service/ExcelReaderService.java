package com.chandu.ResumeEmailSender.service;

import com.chandu.ResumeEmailSender.model.HrDetails;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelReaderService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelReaderService.class);

    @Value("${excel.file.path}")
    private String filePath;

    public List<HrDetails> readHrDetails() {
        List<HrDetails> hrList = new ArrayList<>();

        logger.info("Reading HR details from Excel: {}", filePath);

        try (FileInputStream file = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();

            if (lastRowNum < 1) {
                logger.warn("Excel contains no data rows (only header or empty).");
                return hrList;
            }

            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String companyName = getCellValue(row, 0);
                String hrName = getCellValue(row, 1);
                String email = getCellValue(row, 2);

                if (email.isEmpty() || !isValidEmail(email)) {
                    logger.warn("Skipping invalid email at row {}: {}", i, email);
                    continue;
                }

                // Initializing HrDetails with companyName, hrEmail, hrName, location,
                // experience.
                hrList.add(new HrDetails(companyName, email, hrName, "", ""));
            }

            logger.info("Total Valid HR Records: {}", hrList.size());

        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new RuntimeException("Could not read Excel file", e);
        }

        return hrList;
    }

    private String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null)
            return "";

        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue().trim();
                case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                case FORMULA -> cell.getStringCellValue();
                default -> "";
            };
        } catch (Exception e) {
            return "";
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty())
            return false;
        // More flexible regex for emails with modern TLDs
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
