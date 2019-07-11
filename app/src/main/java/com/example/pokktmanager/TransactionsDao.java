package com.example.pokktmanager;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TransactionsDao {

    @Insert
    public void addTransactions(MoneyAmount moneyAmount);

    @Query("SELECT * FROM transactions WHERE Email LIKE :email AND type LIKE :type ORDER BY transaction_date DESC")
    public List<MoneyAmount> getTransactions(String email, String type);

    @Query("SELECT * FROM transactions WHERE Email LIKE :email ORDER BY transaction_date DESC")
    public List<MoneyAmount> getTransactions(String email);

    @Delete
    public  void delTransaction(MoneyAmount moneyAmount);

    @Query("SELECT * FROM transactions WHERE Email LIKE :email AND tag LIKE '%'||:tag||'%' OR transaction_date LIKE '%'||:tag||'%' ORDER BY transaction_date DESC")
    public List<MoneyAmount> getTransactionsbyTag(String email, String tag);

    @Query("SELECT SUM(amount) FROM transactions WHERE Email LIKE :email AND type LIKE :type " +
            "AND transaction_date > date('now','-7 day')")
    public int getTransactionSummaryWeekly(String email, String type);

    @Query("SELECT SUM(amount) FROM transactions WHERE Email LIKE :email AND type LIKE :type" +
            " AND transaction_date > date('now','-1 month')")
    public int getTransactionSummaryMonthly(String email, String type);

    @Query("SELECT SUM(amount) FROM transactions WHERE Email LIKE :email AND type LIKE :type " +
            "AND transaction_date = date('now','localtime')")
    public int getTransactionSummaryDaily(String email, String type);

    @Query("SELECT SUM(amount) FROM transactions WHERE Email LIKE :email AND type LIKE :type " +
            "AND transaction_date >= :date1 and transaction_date <= :date2")
    public int getTransactionsInDateRange(String email,String type,String date1,String date2);

}
