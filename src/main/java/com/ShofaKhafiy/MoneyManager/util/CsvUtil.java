package com.ShofaKhafiy.MoneyManager.util;

import com.ShofaKhafiy.MoneyManager.model.Transaction;
import com.ShofaKhafiy.MoneyManager.model.User;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CsvUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    // ================= USER CSV =================

    public static String userToCsv(User u) {
        return String.join(",", u.getId(), u.getUsername(), u.getPassword(), String.valueOf(u.isActive()));
    }

    public static User userFromCsv(String line) {
        String[] data = line.split(",");
        return new User(data[0], data[1], data[2], Boolean.parseBoolean(data[3]));
    }

    // ================= TRANSACTION CSV =================

    public static String toCsv(Transaction t) {
        return String.join(",", t.getId(), t.getType(), String.valueOf(t.getAmount()), t.getDescription(),
                String.valueOf(t.getBalanceAfter()), t.getDate().format(DATE_FORMAT));
    }

    public static Transaction fromCsv(String line) {
        String[] data = line.split(",");
        return new Transaction(data[0], data[1], Double.parseDouble(data[2]), data[3], Double.parseDouble(data[4]),
                LocalDate.parse(data[5], DATE_FORMAT));
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
