package com.example.expensemanager.models;

import java.util.Date;

public class Notes {
    private Date dateField;
    private String noteField;

    public Notes(){
    }
    public Notes(Date dateField, String noteField) {
        this.dateField = dateField;
        this.noteField = noteField;
    }

    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    public String getNoteField() {
        return noteField;
    }

    public void setNoteField(String noteField) {
        this.noteField = noteField;
    }
}
