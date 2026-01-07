package com.example.expensemanager.models;

import java.io.Serializable;

public class Alarm implements Serializable {
    private int id;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean status;
    private String name;

    public Alarm(){}

    public Alarm(int year, int month, int day, int hour, int minute, boolean status, String name) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.status = status;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateString() {
        return day + "-" + (month + 1) + "-" + year;
    }

    public String toString(){
        String hourString, minuteString, format;
        if(hour>12){
            hourString= (hour-12)+"";
            format=" PM";
        } else if (hour==0) {
            hourString="12";
            format=" AM";
        } else if (hour==12) {
            hourString="12";
            format=" PM";
        }
        else {
            hourString= hour+"";
            format=" AM";
        }

        if (minute<10){
            minuteString="0"+minute;
        }
        else minuteString=""+minute;
        return hourString + ":" + minuteString + format;
    }
}
