package com.example.expensemanager.models;

public class Account {
    private double accAmount;
    private String accName;

    public Account(){

    }
    public Account(double accAmount, String accName) {
        this.accAmount = accAmount;
        this.accName = accName;
    }

    public double getAccAmount() {
        return accAmount;
    }

    public void setAccAmount(double accAmount) {
        this.accAmount = accAmount;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }
}
