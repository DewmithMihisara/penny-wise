package com.example.expensemanager.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.CustomAdapter;
import com.example.expensemanager.databinding.ActivityReminderBinding;
import com.example.expensemanager.models.Alarm;
import com.example.expensemanager.myHandler.ReminderDBHandler;
import com.example.expensemanager.utils.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static String activeAlarm= "";
    private ListView listView;
    private static final int REQUEST_CODE= 1000;
    public  static List<Alarm> alarmList= new ArrayList<>();
    private CustomAdapter customAdapter;
    Calendar calendar;
    private ReminderDBHandler db= new ReminderDBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        calendar= Calendar.getInstance();

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Log.d("abc", Helper.formatDate(calendar.getTime()));
        getSupportActionBar().setTitle(Helper.formatDate(calendar.getTime()));

        FloatingActionButton button= findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ReminderActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        listView= findViewById(R.id.listView);
        List<Alarm> list= db.getAllAlarms();
        alarmList.addAll(list);
        customAdapter= new CustomAdapter(getApplicationContext(), alarmList);
        listView.setAdapter(customAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_CODE){
            boolean needRefresh= data.getExtras().getBoolean("needRefresh");
            if(needRefresh){
                alarmList.clear();
                List<Alarm> list= db.getAllAlarms();
                alarmList.addAll(list);
                customAdapter.notifyDataSetChanged();
            }
        }
    }
}