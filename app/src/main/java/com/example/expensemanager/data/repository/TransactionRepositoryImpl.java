package com.example.expensemanager.data.repository;

import android.content.Context;

import com.example.expensemanager.data.local.TransactionDatabaseHelper;
import com.example.expensemanager.domain.entity.TransactionEntity;
import com.example.expensemanager.domain.repository.TransactionRepository;
import com.example.expensemanager.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionDatabaseHelper databaseHelper;

    public TransactionRepositoryImpl(Context context) {
        this.databaseHelper = new TransactionDatabaseHelper(context);
    }

    @Override
    public void addTransaction(TransactionEntity transaction) {
        databaseHelper.insertTransaction(transaction);
    }

    @Override
    public void deleteTransaction(TransactionEntity transaction) {
        databaseHelper.deleteTransaction(transaction.getId());
    }

    @Override
    public ArrayList<TransactionEntity> getAllTransactions() {
        return databaseHelper.getAllTransactions();
    }

    @Override
    public ArrayList<TransactionEntity> getTransactionsByDate(long start, long end) {
        return databaseHelper.getTransactionsByDate(start, end);
    }

    @Override
    public double getSumOfIncome(ArrayList<TransactionEntity> transactions) {
        double sum = 0;
        for (TransactionEntity transaction : transactions) {
            if (transaction.getType() != null && transaction.getType().equals(Constants.INCOME)) {
                sum += transaction.getAmount();
            }
        }
        return sum;
    }

    @Override
    public double getSumOfExpense(ArrayList<TransactionEntity> transactions) {
        double sum = 0;
        for (TransactionEntity transaction : transactions) {
            if (transaction.getType() != null && transaction.getType().equals(Constants.EXPENSE)) {
                sum += transaction.getAmount();
            }
        }
        return sum;
    }

    @Override
    public double getSumOfTotal(ArrayList<TransactionEntity> transactions) {
        double sum = 0;
        for (TransactionEntity transaction : transactions) {
            sum += transaction.getAmount();
        }
        return sum;
    }

    @Override
    public double getExpenseSumForMonth(long start, long end) {
        return databaseHelper.getExpenseSumForMonth(start, end);
    }

    @Override
    public double getIncomeSumForMonth(long start, long end) {
        return databaseHelper.getIncomeSumForMonth(start, end);
    }

    @Override
    public HashMap<String, Double> getCategoryPercentage(long start, long end) {
        return databaseHelper.getCategoryPercentage(start, end);
    }

    public ArrayList<com.example.expensemanager.models.Notes> getNotes() {
        return databaseHelper.getNotes();
    }
}
