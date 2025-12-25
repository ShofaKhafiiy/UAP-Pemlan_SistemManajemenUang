package com.ShofaKhafiy.MoneyManager.dao;

;
import com.ShofaKhafiy.MoneyManager.model.Transaction;

import java.util.List;

public interface TransactionDAO {
    void save(Transaction transaction);
    List<Transaction> findAll();
    void update(Transaction transaction);
    void deleteById(String id);
    void clear();
}
