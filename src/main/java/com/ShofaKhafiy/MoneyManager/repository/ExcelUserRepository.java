package com.ShofaKhafiy.MoneyManager.repository;

import com.ShofaKhafiy.MoneyManager.model.User;
import com.ShofaKhafiy.MoneyManager.model.UserRepository;
import com.ShofaKhafiy.MoneyManager.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;

public class ExcelUserRepository implements UserRepository {

    private final String SHEET_NAME = "Users";

    public ExcelUserRepository() {
        ExcelUtil.initAuthDatabase(); // Pastikan file accounts.xlsx ada
    }

    @Override
    public void save(User user) {
        try (Workbook workbook = ExcelUtil.getAuthWorkbook()) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            int lastRow = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRow + 1);

            row.createCell(0).setCellValue(user.getUsername());
            row.createCell(1).setCellValue(user.getPassword());

            ExcelUtil.saveAndCloseAuth(workbook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByUsername(String username) {
        try (Workbook workbook = ExcelUtil.getAuthWorkbook()) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Cell userCell = row.getCell(0);
                if (userCell != null && userCell.getStringCellValue().equals(username)) {
                    String pass = row.getCell(1).getStringCellValue();
                    return new User(username, pass);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exists(String username) {
        return findByUsername(username) != null;
    }
}