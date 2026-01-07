package com.example.expensemanager.myHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expensemanager.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class ReminderDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION= 1;
    private static final String DATABASE_NAME= "Reminder_Manager";
    private static final String TABLE_NAME= "Reminder";

    private static final String COLUMN_ALARM_ID="alarm_ID";
    private static final String COLUMN_ALARM_YEAR="alarm_Year";
    private static final String COLUMN_ALARM_MONTH="alarm_Month";
    private static final String COLUMN_ALARM_DAY="alarm_Day";

    private static final String COLUMN_ALARM_HOUR="alarm_Hour";
    private static final String COLUMN_ALARM_MINUTE="alarm_Minute";
    private static final String COLUMN_ALARM_STATUS="alarm_Status";
    private static final String COLUMN_ALARM_NAME="alarm_Name";

    public ReminderDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ALARM_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ALARM_YEAR + " INTEGER,"
                + COLUMN_ALARM_MONTH + " INTEGER,"
                + COLUMN_ALARM_DAY + " INTEGER,"
                + COLUMN_ALARM_HOUR + " INTEGER,"
                + COLUMN_ALARM_MINUTE + " INTEGER,"
                + COLUMN_ALARM_STATUS + " BOOLEAN,"
                + COLUMN_ALARM_NAME + " STRING"
                + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addAlarm(Alarm alarm){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(COLUMN_ALARM_YEAR, alarm.getYear());
        values.put(COLUMN_ALARM_MONTH, alarm.getMonth());
        values.put(COLUMN_ALARM_DAY, alarm.getDay());
        values.put(COLUMN_ALARM_HOUR, alarm.getHour());
        values.put(COLUMN_ALARM_MINUTE, alarm.getMinute());
        values.put(COLUMN_ALARM_STATUS, alarm.getStatus());
        values.put(COLUMN_ALARM_NAME, alarm.getName());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Alarm> getAllAlarms(){
        List<Alarm> alarmList= new ArrayList<>();
        String selectQuery= "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Alarm alarm= new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setYear(cursor.getInt(1));
                alarm.setMonth(cursor.getInt(2));
                alarm.setDay(cursor.getInt(3));
                alarm.setHour(cursor.getInt(4));
                alarm.setMinute(cursor.getInt(5));
                alarm.setStatus(cursor.getInt(6) !=0 );
                alarm.setName(cursor.getString(7));
                alarmList.add(alarm);
            }while (cursor.moveToNext());
        }
        return alarmList;
    }

    public int updateAlarm(Alarm alarm){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(COLUMN_ALARM_YEAR, alarm.getYear());
        values.put(COLUMN_ALARM_MONTH, alarm.getMonth());
        values.put(COLUMN_ALARM_DAY, alarm.getDay());
        values.put(COLUMN_ALARM_HOUR, alarm.getHour());
        values.put(COLUMN_ALARM_MINUTE, alarm.getMinute());
        values.put(COLUMN_ALARM_STATUS, alarm.getStatus());
        values.put(COLUMN_ALARM_NAME, alarm.getName());

        return db.update(TABLE_NAME, values, COLUMN_ALARM_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())});
    }
}
