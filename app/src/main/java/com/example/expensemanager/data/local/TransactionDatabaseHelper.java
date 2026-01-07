package com.example.expensemanager.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expensemanager.domain.entity.TransactionEntity;
import com.example.expensemanager.data.local.DatabaseConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TransactionDatabaseHelper extends SQLiteOpenHelper {

    public TransactionDatabaseHelper(Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DatabaseConstants.TRANS_TABLE + "(" +
                DatabaseConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseConstants.TYPE + " TEXT," +
                DatabaseConstants.CATEGORY + " TEXT," +
                DatabaseConstants.ACCOUNT + " TEXT," +
                DatabaseConstants.NOTE + " TEXT DEFAULT '0'," +
                DatabaseConstants.DATE + " INTEGER," +
                DatabaseConstants.AMOUNT + " REAL" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertTransaction(TransactionEntity transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.TYPE, transaction.getType());
        values.put(DatabaseConstants.CATEGORY, transaction.getCategory());
        values.put(DatabaseConstants.ACCOUNT, transaction.getAccount());
        values.put(DatabaseConstants.NOTE, transaction.getNote());

        long dateMillis;
        if (transaction.getDate() != null) {
            dateMillis = transaction.getDate().getTime();
        } else {
            dateMillis = new Date().getTime();
        }
        values.put(DatabaseConstants.DATE, dateMillis);
        values.put(DatabaseConstants.AMOUNT, (float) transaction.getAmount());

        long id = db.insert(DatabaseConstants.TRANS_TABLE, null, values);
        db.close();
        return id;
    }

    public ArrayList<TransactionEntity> getAllTransactions() {
        ArrayList<TransactionEntity> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + DatabaseConstants.TRANS_TABLE;
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(mapCursorToTransaction(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<TransactionEntity> getTransactionsByDate(long start, long end) {
        ArrayList<TransactionEntity> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, DatabaseConstants.TRANS_TABLE)) {
            db.close();
            return list;
        }

        String select = "SELECT * FROM " + DatabaseConstants.TRANS_TABLE +
                " WHERE " + DatabaseConstants.DATE + " BETWEEN ? AND ?";
        String[] selectionArgs = {String.valueOf(start), String.valueOf(end)};
        Cursor cursor = db.rawQuery(select, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                list.add(mapCursorToTransaction(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteTransaction(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseConstants.TRANS_TABLE, DatabaseConstants.ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public double getExpenseSumForMonth(long start, long end) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, DatabaseConstants.TRANS_TABLE)) {
            db.close();
            return 0;
        }

        String select = "SELECT SUM(" + DatabaseConstants.AMOUNT + ") FROM " + DatabaseConstants.TRANS_TABLE +
                " WHERE " + DatabaseConstants.TYPE + " = ? AND " + DatabaseConstants.DATE + " >= ? AND " + DatabaseConstants.DATE + " <= ?";
        String[] selectionArgs = {com.example.expensemanager.utils.Constants.EXPENSE, String.valueOf(start), String.valueOf(end)};

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

    public double getIncomeSumForMonth(long start, long end) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, DatabaseConstants.TRANS_TABLE)) {
            db.close();
            return 0;
        }

        String select = "SELECT SUM(" + DatabaseConstants.AMOUNT + ") FROM " + DatabaseConstants.TRANS_TABLE +
                " WHERE " + DatabaseConstants.TYPE + " = ? AND " + DatabaseConstants.DATE + " >= ? AND " + DatabaseConstants.DATE + " <= ?";
        String[] selectionArgs = {com.example.expensemanager.utils.Constants.INCOME, String.valueOf(start), String.valueOf(end)};

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

    public HashMap<String, Double> getCategoryPercentage(long start, long end) {
        HashMap<String, Double> categoryPercentageMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, DatabaseConstants.TRANS_TABLE)) {
            db.close();
            return categoryPercentageMap;
        }

        String query = "SELECT " + DatabaseConstants.AMOUNT + "," + DatabaseConstants.CATEGORY +
                " FROM " + DatabaseConstants.TRANS_TABLE +
                " WHERE " + DatabaseConstants.TYPE + " = ? AND " + DatabaseConstants.DATE + " BETWEEN ? AND ?";
        String[] selectionArgs = {com.example.expensemanager.utils.Constants.EXPENSE, String.valueOf(start), String.valueOf(end)};
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

            if (amount < 0) {
                amount = amount * (-1);
            }
            totalAmount += amount;
        }

        cursor.close();
        db.close();

        for (String category : categoryAmountMap.keySet()) {
            double amount = categoryAmountMap.get(category);
            double percentage = (-1) * ((amount / totalAmount) * 100);
            categoryPercentageMap.put(category, percentage);
        }

        return categoryPercentageMap;
    }

    public ArrayList<com.example.expensemanager.models.Notes> getNotes() {
        ArrayList<com.example.expensemanager.models.Notes> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, DatabaseConstants.TRANS_TABLE)) {
            db.close();
            return list;
        }

        String select = "SELECT " + DatabaseConstants.DATE + ", " + DatabaseConstants.NOTE +
                " FROM " + DatabaseConstants.TRANS_TABLE +
                " WHERE " + DatabaseConstants.NOTE + " IS NOT NULL AND " +
                DatabaseConstants.NOTE + " != ''";
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                com.example.expensemanager.models.Notes notes = new com.example.expensemanager.models.Notes();
                long dateInMillis = cursor.getLong(0);
                notes.setDateField(new Date(dateInMillis));
                notes.setNoteField(cursor.getString(1));
                list.add(notes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    private TransactionEntity mapCursorToTransaction(Cursor cursor) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(cursor.getInt(0));
        transaction.setType(cursor.getString(1));
        transaction.setCategory(cursor.getString(2));
        transaction.setAccount(cursor.getString(3));
        transaction.setNote(cursor.getString(4));
        long dateInMillis = cursor.getLong(5);
        transaction.setDate(new Date(dateInMillis));
        transaction.setAmount(cursor.getFloat(6));
        return transaction;
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}

