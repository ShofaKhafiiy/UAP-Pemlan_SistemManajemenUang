package com.ShofaKhafiy.MoneyManager.util;

import com.ShofaKhafiy.MoneyManager.model.FinancialRecord;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CsvUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    // ================= FINANCIAL RECORD CSV =================

    public static String toCsv(FinancialRecord record) {
        return String.join(",", record.getId(), record.getType(), String.valueOf(record.getAmount()),
                record.getDescription(), record.getDate().format(DATE_FORMAT));
    }

    public static FinancialRecord fromCsv(String line) {
        String[] data = line.split(",");
        // Cek panjang data sebelum melakukan parsing tanggal
        if (data.length == 5) {  // Pastikan ada 5 kolom: id, type, amount, description, date
            try {
                return new FinancialRecord(data[0], data[1], Double.parseDouble(data[2]), data[3], LocalDate.parse(data[4], DATE_FORMAT));
            } catch (Exception e) {
                System.out.println("Error parsing data: " + e.getMessage());
                return null;  // Return null if parsing fails
            }
        } else {
            System.out.println("Invalid data format in CSV: " + line);
            return null;  // Invalid format
        }
    }

    // ================= FILE UTILITY =================

    public static void ensureFileExists(String filePath) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to ensure file exists", e);
        }
    }
}
