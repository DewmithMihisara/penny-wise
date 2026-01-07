package com.example.expensemanager.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensemanager.data.mapper.TransactionMapper;
import com.example.expensemanager.data.repository.TransactionRepositoryImpl;
import com.example.expensemanager.domain.entity.TransactionEntity;
import com.example.expensemanager.domain.repository.TransactionRepository;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainViewModel extends AndroidViewModel {

    private final TransactionRepository repository;
    private Calendar calendar;

    public MutableLiveData<ArrayList<Transaction>> transactionMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TransactionRepositoryImpl(application);
    }

    public void getTransactions(Calendar calendar) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long start;
        long end;

        if (Constants.SELECTED_TAB == Constants.DAILY) {
            start = calendar.getTimeInMillis();
            Date date = new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000));
            end = date.getTime();
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            start = calendar.getTimeInMillis();
            calendar.add(Calendar.MONTH, 1);
            end = calendar.getTimeInMillis();
        } else {
            start = 0;
            end = System.currentTimeMillis();
        }

        ArrayList<TransactionEntity> entities = repository.getTransactionsByDate(start, end);
        ArrayList<Transaction> transactions = TransactionMapper.toModelList(entities);
        transactionMutableLiveData.setValue(transactions);

        double income = repository.getSumOfIncome(entities);
        double expense = repository.getSumOfExpense(entities);
        double total = repository.getSumOfTotal(entities);

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
    }

    public void addTransaction(Transaction transaction) {
        TransactionEntity entity = TransactionMapper.toEntity(transaction);
        repository.addTransaction(entity);
    }

    public void deleteTransaction(Transaction transaction) {
        TransactionEntity entity = TransactionMapper.toEntity(transaction);
        repository.deleteTransaction(entity);
        if (calendar != null) {
            getTransactions(calendar);
        }
    }

    public ArrayList<Integer> getIncomeForLast6Months() {
        ArrayList<Integer> incomeSumList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -5);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 6; i++) {
            long start = cal.getTimeInMillis();

            long end;
            if (i == 5) {
                end = Calendar.getInstance().getTimeInMillis();
            } else {
                Calendar endCal = (Calendar) cal.clone();
                endCal.add(Calendar.MONTH, 1);
                end = endCal.getTimeInMillis();
            }

            double incomeSum = repository.getIncomeSumForMonth(start, end);
            incomeSumList.add((int) incomeSum);

            cal.add(Calendar.MONTH, 1);
        }

        return incomeSumList;
    }

    public ArrayList<Integer> getExpenseForLast6Months() {
        ArrayList<Integer> expenseSumList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -5);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 6; i++) {
            long start = cal.getTimeInMillis();

            long end;
            if (i == 5) {
                Calendar endCal = Calendar.getInstance();
                endCal.set(Calendar.HOUR_OF_DAY, 23);
                endCal.set(Calendar.MINUTE, 59);
                endCal.set(Calendar.SECOND, 59);
                endCal.set(Calendar.MILLISECOND, 999);
                end = endCal.getTimeInMillis();
            } else {
                Calendar endCal = (Calendar) cal.clone();
                endCal.add(Calendar.MONTH, 1);
                endCal.add(Calendar.MILLISECOND, -1);
                end = endCal.getTimeInMillis();
            }

            double expenseSum = repository.getExpenseSumForMonth(start, end);
            if (expenseSum < 0) {
                expenseSum = expenseSum * (-1);
            }
            expenseSumList.add((int) expenseSum);

            cal.add(Calendar.MONTH, 1);
        }

        return expenseSumList;
    }

    public HashMap<String, Double> getCategoryPercentage(Calendar calendar) {
        this.calendar = calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        long start = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        long end = calendar.getTimeInMillis();

        return repository.getCategoryPercentage(start, end);
    }
}

