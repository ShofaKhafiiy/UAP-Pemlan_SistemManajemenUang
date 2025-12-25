package com.ShofaKhafiy.MoneyManager.dao;

import com.ShofaKhafiy.MoneyManager.model.FinancialRecord;
import java.util.List;

public interface FinancialRecordDAO {
    void save(FinancialRecord financialRecord);
    List<FinancialRecord> findAll();
    void update(FinancialRecord financialRecord);
    void deleteById(String id);
    void clear();
}
