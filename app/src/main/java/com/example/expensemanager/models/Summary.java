package com.example.expensemanager.models;

public class Summary {

    private String categoryType;
    private double amount, perAmount;

    public Summary(){
    }

    public Summary(String category, double amount, double perAmount) {
        this.categoryType = category;
        this.amount = amount;
        this.perAmount = perAmount;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String category) {
        this.categoryType = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPerAmount() {
        return perAmount;
    }

    public void setPerAmount(double perAmount) {
        this.perAmount = perAmount;
    }
}
