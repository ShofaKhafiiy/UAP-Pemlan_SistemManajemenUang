package com.ShofaKhafiy.MoneyManager.repository;

import com.ShofaKhafiy.MoneyManager.model.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryTransactionRepo implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    // ================= CREATE =================
    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    // ================= READ =================
    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    // ================= UPDATE =================
    @Override
    public boolean update(String id, Transaction updatedTransaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(id)) {
                transactions.set(i, updatedTransaction); // Update transaksi berdasarkan id
                return true;
            }
        }
        return false;
    }


    // ================= DELETE =================
    @Override
    public boolean deleteById(String id) {

        Iterator<Transaction> iterator = transactions.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }



    // ================= SYSTEM =================
    @Override
    public void clear() {
        transactions.clear();
    }
}
