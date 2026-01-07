package com.example.expensemanager.myHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.expensemanager.models.Notes;
import com.example.expensemanager.models.Summary;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class transDBHandler extends SQLiteOpenHelper {

    public transDBHandler(Context context){
        super(context, parameters.DB_NAME,null, parameters.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create= "CREATE TABLE "+parameters.TRANS_TABLE+"("+parameters.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+parameters.TYPE+" TEXT,"+parameters.CATEGORY+" TEXT,"+parameters.ACCOUNT+" TEXT,"+parameters.NOTE+" TEXT DEFAULT '0',"+parameters.DATE+" INTEGER,"+parameters.AMOUNT+" REAL"+")";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addTransaction(Transaction transaction){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(parameters.TYPE, transaction.getType());
        values.put(parameters.CATEGORY, transaction.getCategory());
        values.put(parameters.ACCOUNT, transaction.getAccount());
        values.put(parameters.NOTE, transaction.getNote());
        
        long dateMillis;
        if (transaction.getDate() != null) {
            dateMillis = transaction.getDate().getTime();
        } else {
            dateMillis = new Date().getTime();
        }
        values.put(parameters.DATE, dateMillis);
        values.put(parameters.AMOUNT, (float)transaction.getAmount());

        db.insert(parameters.TRANS_TABLE, null, values);
        db.close();
    }

    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Transaction> list= new ArrayList<>();
        SQLiteDatabase db= this.getReadableDatabase();

        String select= "SELECT * FROM "+parameters.TRANS_TABLE;
        Cursor cursor= db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                Transaction transaction= new Transaction();
                transaction.setId(cursor.getInt(0));
                transaction.setType(cursor.getString(1));
                transaction.setCategory(cursor.getString(2));
                transaction.setAccount(cursor.getString(3));
                transaction.setNote(cursor.getString(4));
                long dateInMillis = cursor.getLong(5);
                transaction.setDate(new Date(dateInMillis));
                transaction.setAmount(cursor.getFloat(6));
                list.add(transaction);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<Transaction> getTransactionsByDate(Long start, Long end) {
        ArrayList<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, parameters.TRANS_TABLE)) {
            db.close();
            return list;
        }

        String select = "SELECT * FROM " + parameters.TRANS_TABLE + " WHERE " + parameters.DATE + " BETWEEN ? AND ?" ;
        String[] selectionArgs = { String.valueOf(start), String.valueOf(end) };
        Cursor cursor = db.rawQuery(select, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(0));
                transaction.setType(cursor.getString(1));
                transaction.setCategory(cursor.getString(2));
                transaction.setAccount(cursor.getString(3));
                transaction.setNote(cursor.getString(4));
                long dateInMillis = cursor.getLong(5);
                transaction.setDate(new Date(dateInMillis));
                transaction.setAmount(cursor.getFloat(6));
                list.add(transaction);
            } while (cursor.moveToNext());
        }
        else{
            Log.d("abc","no record");
        }
        cursor.close();
        return list;
    }
    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[] { tableName });
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public double getSumOfIncome(ArrayList<Transaction> transactions) {
        double sum = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() != null && transaction.getType().equals(Constants.INCOME)) {
                sum += transaction.getAmount();
            }
        }
        return sum;
    }

    public double getSumOfExpense(ArrayList<Transaction> transactions) {
        double sum = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() != null && transaction.getType().equals(Constants.EXPENSE)) {
                sum += transaction.getAmount();
            }
        }
        return sum;
    }

    public double getSumOfTotal(ArrayList<Transaction> transactions) {
        double sum = 0;
        for (Transaction transaction : transactions) {
            sum += transaction.getAmount();
        }
        return sum;
    }

    public void deleteTransaction(Transaction transaction){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(parameters.TRANS_TABLE, parameters.ID + " = ?", new String[]{String.valueOf(transaction.getId())});
        db.close();
    }

    public double getExpenseSumForMonth(Long start, Long end) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, parameters.TRANS_TABLE)) {
            db.close();
            return 0;
        }

        String select = "SELECT SUM(" + parameters.AMOUNT + ") FROM " + parameters.TRANS_TABLE + " WHERE " + parameters.TYPE + " = ? AND " + parameters.DATE + " >= ? AND " + parameters.DATE + " <= ?";
        String[] selectionArgs = { Constants.EXPENSE, String.valueOf(start), String.valueOf(end) };

        Cursor cursor = db.rawQuery(select, selectionArgs);
        double expenseSum = 0;

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            Double result = cursor.getDouble(0);
            if (result != null && !Double.isNaN(result)) {
                expenseSum = result;
            }
        }

        cursor.close();
        db.close();
        return expenseSum;
    }

    public double getIncomeSumForMonth(Long start, Long end) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, parameters.TRANS_TABLE)) {
            db.close();
            return 0;
        }

        String select = "SELECT SUM(" + parameters.AMOUNT + ") FROM " + parameters.TRANS_TABLE + " WHERE " + parameters.TYPE + " = ? AND " + parameters.DATE + " >= ? AND " + parameters.DATE + " <= ?";
        String[] selectionArgs = { Constants.INCOME, String.valueOf(start), String.valueOf(end) };

        Cursor cursor = db.rawQuery(select, selectionArgs);
        double incomeSum = 0;

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            Double result = cursor.getDouble(0);
            if (result != null && !Double.isNaN(result)) {
                incomeSum = result;
            }
        }

        cursor.close();
        db.close();
        return incomeSum;
    }


    public HashMap<String, Double> getCategory(Long start, Long end){
        HashMap<String, Double> categoryPercentageMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, parameters.TRANS_TABLE)) {
            db.close();
            return categoryPercentageMap;
        }

        String query = "SELECT "+parameters.AMOUNT+","+parameters.CATEGORY+" FROM "+parameters.TRANS_TABLE+" WHERE " + parameters.TYPE + " = ? AND " + parameters.DATE + " BETWEEN ? AND ?";
        String[] selectionArgs = { Constants.EXPENSE, String.valueOf(start), String.valueOf(end) };
        Cursor cursor = db.rawQuery(query, selectionArgs);

        HashMap<String, Double> categoryAmountMap = new HashMap<>();
        double totalAmount = 0.0;

        while (cursor.moveToNext()) {
            double amount = cursor.getDouble(0);
            String category = cursor.getString(1);

            if (categoryAmountMap.containsKey(category)) {
                double currentAmount = categoryAmountMap.get(category);
                categoryAmountMap.put(category, currentAmount + amount);
            } else {
                categoryAmountMap.put(category, amount);
            }

            if(amount<0){
                amount=amount*(-1);
            }
            totalAmount += amount;
        }

        cursor.close();
        db.close();

        for (Map.Entry<String, Double> entry : categoryAmountMap.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            double percentage = (-1)*((amount / totalAmount) * 100);

            categoryPercentageMap.put(category, percentage);

        }

        return categoryPercentageMap;
    }

    public ArrayList<Notes> getNotes(){
        ArrayList<Notes> list= new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, parameters.TRANS_TABLE)) {
            db.close();
            return list;
        }

        String select = "SELECT " + parameters.DATE + ", " + parameters.NOTE + " FROM " + parameters.TRANS_TABLE + " WHERE " + parameters.NOTE + " IS NOT NULL AND " + parameters.NOTE + " != ''";
        Cursor cursor= db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                 Notes notes= new Notes();
                 long dateInMillis = cursor.getLong(0);
                 notes.setDateField(new Date(dateInMillis));
                 notes.setNoteField(cursor.getString(1));
                 list.add(notes);
            }while (cursor.moveToNext());
        }
        return list;
    }
}
