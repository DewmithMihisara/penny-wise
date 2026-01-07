package com.example.expensemanager.domain.repository;

import com.example.expensemanager.domain.entity.TransactionEntity;

import java.util.ArrayList;
import java.util.HashMap;

public interface TransactionRepository {
    
    void addTransaction(TransactionEntity transaction);
    
    void deleteTransaction(TransactionEntity transaction);
    
    ArrayList<TransactionEntity> getAllTransactions();
    
    ArrayList<TransactionEntity> getTransactionsByDate(long start, long end);
    
    double getSumOfIncome(ArrayList<TransactionEntity> transactions);
    
    double getSumOfExpense(ArrayList<TransactionEntity> transactions);
    
    double getSumOfTotal(ArrayList<TransactionEntity> transactions);
    
    double getExpenseSumForMonth(long start, long end);
    
    double getIncomeSumForMonth(long start, long end);
    
    HashMap<String, Double> getCategoryPercentage(long start, long end);
}

