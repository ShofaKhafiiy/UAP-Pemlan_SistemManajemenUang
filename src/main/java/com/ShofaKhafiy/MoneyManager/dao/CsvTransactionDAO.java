package com.ShofaKhafiy.MoneyManager.dao;


import com.ShofaKhafiy.MoneyManager.model.Transaction;
import com.ShofaKhafiy.MoneyManager.util.CsvUtil;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvTransactionDAO implements TransactionDAO {

    private final String filePath;

    public CsvTransactionDAO(String filePath) {
        this.filePath = filePath;
        CsvUtil.ensureFileExists(filePath); // Ensure file exists
    }

    @Override
    public void save(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(CsvUtil.toCsv(transaction));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error saving transaction.", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    transactions.add(CsvUtil.fromCsv(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading transactions.", e);
        }
        return transactions;
    }

    @Override
    public void update(Transaction transaction) {
        List<Transaction> all = findAll();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Transaction t : all) {
                if (t.getId().equals(transaction.getId())) {
                    writer.write(CsvUtil.toCsv(transaction));
                } else {
                    writer.write(CsvUtil.toCsv(t));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error updating transaction.", e);
        }
    }

    @Override
    public void deleteById(String id) {
        List<Transaction> all = findAll();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Transaction t : all) {
                if (!t.getId().equals(id)) {
                    writer.write(CsvUtil.toCsv(t));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting transaction.", e);
        }
    }

    @Override
    public void clear() {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            CsvUtil.ensureFileExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error clearing file.", e);
        }
    }
}
