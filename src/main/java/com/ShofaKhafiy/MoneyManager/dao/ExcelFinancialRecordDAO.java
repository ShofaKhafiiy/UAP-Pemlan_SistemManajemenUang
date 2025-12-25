package com.ShofaKhafiy.MoneyManager.dao;

import com.ShofaKhafiy.MoneyManager.model.FinancialRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelFinancialRecordDAO {

    // Save record to Excel file for specific user
    public void save(FinancialRecord financialRecord, String userId) {
        String userFilePath = "transactions_user_" + userId + ".xlsx"; // User-specific file
        try (FileInputStream fis = new FileInputStream(userFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the sheet or create one if it doesn't exist
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                sheet = workbook.createSheet("Transactions");
            }

            // Create new row and fill with transaction data
            Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            row.createCell(0).setCellValue(financialRecord.getId());
            row.createCell(1).setCellValue(financialRecord.getType());
            row.createCell(2).setCellValue(financialRecord.getAmount());
            row.createCell(3).setCellValue(financialRecord.getDescription());
            row.createCell(4).setCellValue(financialRecord.getDate().toString());

            try (FileOutputStream fos = new FileOutputStream(userFilePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            // If file doesn't exist yet, create new file
            createNewTransactionFile(financialRecord, userId);
        }
    }

    // Helper method to create a new file if the user does not have an existing one
    private void createNewTransactionFile(FinancialRecord financialRecord, String userId) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Type");
        headerRow.createCell(2).setCellValue("Amount");
        headerRow.createCell(3).setCellValue("Description");
        headerRow.createCell(4).setCellValue("Date");

        // Create a row with transaction data
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(financialRecord.getId());
        row.createCell(1).setCellValue(financialRecord.getType());
        row.createCell(2).setCellValue(financialRecord.getAmount());
        row.createCell(3).setCellValue(financialRecord.getDescription());
        row.createCell(4).setCellValue(financialRecord.getDate().toString());

        try (FileOutputStream fos = new FileOutputStream("transactions_user_" + userId + ".xlsx")) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read all transactions from the user's Excel file
    public List<FinancialRecord> findAll(String userId) {
        List<FinancialRecord> records = new ArrayList<>();
        String userFilePath = "transactions_user_" + userId + ".xlsx"; // User-specific file
        try (FileInputStream fis = new FileInputStream(userFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet (Transactions)
            int rows = sheet.getPhysicalNumberOfRows();

            // Skip the header row and iterate over the data rows
            for (int i = 1; i < rows; i++) {
                Row row = sheet.getRow(i);
                String id = row.getCell(0).getStringCellValue();
                String type = row.getCell(1).getStringCellValue();
                double amount = row.getCell(2).getNumericCellValue();
                String description = row.getCell(3).getStringCellValue();
                String date = row.getCell(4).getStringCellValue(); // Get date as string

                // Convert the string date to LocalDate
                FinancialRecord record = new FinancialRecord(id, type, amount, description, LocalDate.parse(date));
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
