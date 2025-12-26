package com.ShofaKhafiy.MoneyManager.util;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility untuk formatting data (tanggal, uang, persen)
 * Single Responsibility Principle
 */
public class FormatUtil {

    // Format untuk tanggal dengan tipe LocalDate
    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    // Format untuk tanggal dengan tipe LocalDateTime
    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    // Format untuk uang dengan simbol mata uang sesuai Locale
    public static String formatCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return currencyFormatter.format(amount);
    }

    // Format untuk persen
    public static String formatPercentage(double value) {
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();
        percentFormatter.setMaximumFractionDigits(2); // Menampilkan 2 digit setelah koma
        return percentFormatter.format(value);
    }
}
