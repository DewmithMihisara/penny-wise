package com.example.expensemanager.views.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityAddBinding;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.example.expensemanager.models.Alarm;
import com.example.expensemanager.myHandler.ReminderDBHandler;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    int year, month, day, hour, minute;
    private Alarm alarm;
    private boolean needRefresh;
    ActivityAddBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar3);
        getSupportActionBar().setTitle("Set Reminder");

        alarm= new Alarm();
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        alarm.setYear(year);
                        alarm.setMonth(month);
                        alarm.setDay(dayOfMonth);
                        binding.datePicker.setText(dayOfMonth+"-"+month+"-"+year);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });

        binding.timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                alarm.setHour(hourOfDay);
                                alarm.setMinute(minute);
                                binding.timePicker.setText(alarm.toString());
                                view.clearFocus();
                            }
                        }, hour, minute, false);

                timePickerDialog.show();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= binding.name.getText().toString();

                ReminderDBHandler db= new ReminderDBHandler(getApplicationContext());

                alarm.setStatus(true);
                alarm.setName(name);
                db.addAlarm(alarm);

                needRefresh= true;
                onBackPressed();
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void finish() {
        Intent data= new Intent();
        data.putExtra("needRefresh", needRefresh);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}
