package com.example.android.grouptripexpense.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expense")
    LiveData<List<ExpenseEntry>> loadAllExpensesLivedata();

    @Query("SELECT * FROM expense")
    List<ExpenseEntry> loadAllExpenses();

    @Insert
    void insertExpense(ExpenseEntry expenseEntry);

    @Query("Delete FROM expense")
    void deleteAllExpenses();
}
