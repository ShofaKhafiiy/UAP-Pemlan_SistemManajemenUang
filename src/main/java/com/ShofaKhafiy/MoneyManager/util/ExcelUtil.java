package com.ShofaKhafiy.MoneyManager.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class ExcelUtil {
    // Gunakan folder relatif "data"
    private static final String BASE_DIR = "data";
    private static final String USER_DIR = BASE_DIR + "/users/";
    private static final String AUTH_FILE = BASE_DIR + "/accounts.xlsx";

    public static void initDirectories() {
        File base = new File(BASE_DIR);
        File users = new File(USER_DIR);

        if (!base.exists()) {
            boolean created = base.mkdirs();
            System.out.println("Membuat folder data: " + (created ? "Berhasil" : "Gagal"));
        }
        if (!users.exists()) {
            users.mkdirs();
        }

        // Cetak lokasi absolut agar Anda bisa mencari foldernya di Windows Explorer
        System.out.println("Lokasi database: " + base.getAbsolutePath());
    }

    public static void initAuthDatabase() {
        initDirectories();
        File file = new File(AUTH_FILE);

        if (!file.exists()) {
            System.out.println("Mencoba membuat file accounts.xlsx...");
            try (Workbook workbook = new XSSFWorkbook();
                 FileOutputStream fileOut = new FileOutputStream(file)) {

                Sheet sheet = workbook.createSheet("Users");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Username");
                header.createCell(1).setCellValue("Password");

                workbook.write(fileOut);
                fileOut.flush(); // Pastikan data terdorong ke disk
                System.out.println("File accounts.xlsx BERHASIL dibuat di: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("GAGAL membuat accounts.xlsx: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void initUserDatabase(String username) {
        File file = new File(USER_DIR + "db_" + username + ".xlsx");
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook();
                 FileOutputStream fileOut = new FileOutputStream(file)) {

                Sheet trxSheet = workbook.createSheet("Transactions");
                Row trxHeader = trxSheet.createRow(0);
                String[] columns = {"ID", "Date", "Type", "Category", "Description", "Amount", "BalanceAfter", "IsDeleted"};
                for (int i = 0; i < columns.length; i++) {
                    trxHeader.createCell(i).setCellValue(columns[i]);
                }

                workbook.write(fileOut);
                fileOut.flush();
                System.out.println("File database user " + username + " berhasil dibuat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // --- Method pendukung lainnya tetap sama ---
    public static Workbook getAuthWorkbook() throws IOException {
        return WorkbookFactory.create(new FileInputStream(AUTH_FILE));
    }

    public static void saveAndCloseAuth(Workbook workbook) {
        try (FileOutputStream fos = new FileOutputStream(AUTH_FILE)) {
            workbook.write(fos);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static Workbook getWorkbook(String username) throws IOException {
        return WorkbookFactory.create(new FileInputStream(USER_DIR + "db_" + username + ".xlsx"));
    }

    public static void saveAndClose(Workbook workbook, String username) {
        try (FileOutputStream fos = new FileOutputStream(USER_DIR + "db_" + username + ".xlsx")) {
            workbook.write(fos);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static String getUserFilePath(String username) {
        return USER_DIR + "db_" + username + ".xlsx";
    }
}