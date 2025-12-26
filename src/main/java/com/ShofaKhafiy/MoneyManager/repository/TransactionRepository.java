package com.ShofaKhafiy.MoneyManager.repository;

import com.ShofaKhafiy.MoneyManager.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    // CREATE
    void save(Transaction transaction);

    // READ
    List<Transaction> findAll();

    // UPDATE
    boolean update(String id, Transaction updatedTransaction);

    // DELETE
    boolean deleteById(String id);

    // SYSTEM
    void clear();
}
