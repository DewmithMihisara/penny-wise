package com.example.expensemanager.utils;

import com.example.expensemanager.R;
import com.example.expensemanager.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME= "INCOME";
    public static String EXPENSE= "EXPENSE";

    public static ArrayList<Category> categories;

    public static int DAILY= 0;
    public static int MONTHLY= 1;
    public static int CALENDER= 2;
    public static int SUMMARY= 3;
    public static int NOTES= 4;
    public static int SELECTED_TAB=0;
    public static void setCategories(){
        categories= new ArrayList<>();
        categories.add(new Category(R.drawable.ic_salary,"Salary",R.color.purple));
        categories.add(new Category(R.drawable.ic_invest,"Business",R.color.cashColor));
        categories.add(new Category(R.drawable.ic_loan,"Rent/Loan",R.color.light_blue));
        categories.add(new Category(R.drawable.ic_groceries,"Groceries",R.color.green));
        categories.add(new Category(R.drawable.ic_education,"Education",R.color.blue));
        categories.add(new Category(R.drawable.ic_entertain,"Entertainment",R.color.red));
        categories.add(new Category(R.drawable.ic_other,"Other",R.color.yellow));
    }

    public static Category getCategorieDetails(String categoryName){
        for(Category cat: categories){
            if(cat.getCategoryName().equals(categoryName)) return cat;
        }
        return null ;
    }

    public static int getAccountColor(String accountName){
        switch (accountName){
            case "Bank":
                return R.color.bankColor;
            case "Cash":
                return R.color.cashColor;
            case "Card":
                return R.color.cardColor;
            default:
                return R.color.defaultColor;
        }
    }
}
