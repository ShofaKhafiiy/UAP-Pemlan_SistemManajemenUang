package com.ShofaKhafiy.MoneyManager.repository;

import com.ShofaKhafiy.MoneyManager.model.*;
import com.ShofaKhafiy.MoneyManager.util.ExcelUtil;
import com.ShofaKhafiy.MoneyManager.service.CategoryFactory;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelTransactionRepository implements TransactionRepository {
    private final String username;

    public ExcelTransactionRepository(String username) {
        this.username = username;
        // Memastikan folder dan file database user ini sudah dibuat
        ExcelUtil.initUserDatabase(username);
    }

    @Override
    public void save(Transaction t) {
        try (Workbook workbook = ExcelUtil.getWorkbook(username)) {
            Sheet sheet = workbook.getSheetAt(0); // Mengambil sheet "Transactions"
            int lastRow = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRow + 1);

            String categoryName = (t.getCategory() != null) ? t.getCategory().getName() : "Lainnya";
            // URUTAN KOLOM HARUS SAMA DENGAN ExcelUtil.initUserDatabase
            row.createCell(0).setCellValue(t.getId());
            row.createCell(1).setCellValue(t.getCreatedAt().toString()); // Simpan sebagai String (ISO-8601)
            row.createCell(2).setCellValue(t.getType().name());         // INCOME/EXPENSE
            row.createCell(3).setCellValue(t.getCategory().getName());
            row.createCell(4).setCellValue(t.getDescription());
            row.createCell(5).setCellValue(t.getAmount());
            row.createCell(6).setCellValue(t.getBalanceAfter());
            row.createCell(7).setCellValue(t.isDeleted());

            ExcelUtil.saveAndClose(workbook, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        try (Workbook workbook = ExcelUtil.getWorkbook(username)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                // Skip Header (Baris 0)
                if (row.getRowNum() == 0) continue;

                // Ambil data berdasarkan index kolom yang sama saat save()
                String id = row.getCell(0).getStringCellValue();
                LocalDate date = LocalDate.parse(row.getCell(1).getStringCellValue());
                TransactionType type = TransactionType.valueOf(row.getCell(2).getStringCellValue());

                // Cari kategori yang sesuai dari Factory
                Category cat = CategoryFactory.getInstance().getCategory(
                        row.getCell(3).getStringCellValue(),
                        type
                );

                Transaction t = new Transaction.Builder(id, type)
                        .owner(this.username) // Owner diambil dari konteks file ini
                        .setCreatedAt(date)
                        .category(cat)
                        .description(row.getCell(4).getStringCellValue())
                        .amount(row.getCell(5).getNumericCellValue())
                        .balanceAfter(row.getCell(6).getNumericCellValue())
                        .deleted(row.getCell(7).getBooleanCellValue())
                        .build();
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void clear() {
        try (Workbook workbook = ExcelUtil.getWorkbook(username)) {
            Sheet sheet = workbook.getSheetAt(0);
            // Menghapus semua baris kecuali header
            for (int i = sheet.getLastRowNum(); i > 0; i--) {
                Row r = sheet.getRow(i);
                if (r != null) sheet.removeRow(r);
            }
            ExcelUtil.saveAndClose(workbook, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Implementasi Tambahan untuk Interface TransactionRepository ---

    @Override
    public boolean update(String id, Transaction updated) {
        // Logika update di service memanggil findAll() -> clear() -> save()
        // Jadi kita hanya perlu mengembalikan true jika save berhasil.
        save(updated);
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        // Sama dengan update, logika soft delete sudah ditangani di Service
        return true;
    }
}