package com.example.expensemanager.models;

public class Category {

    private int categoryImg;
    private String categoryName;
    private int categoryColor;

    public Category(int categoryImg, String categoryName, int categoryColor) {
        this.categoryImg = categoryImg;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

    public Category(){

    }

    public Category(int categoryImg, String categoryName) {
        this.categoryImg = categoryImg;
        this.categoryName = categoryName;
    }

    public int getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(int categoryImg) {
        this.categoryImg = categoryImg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

}
