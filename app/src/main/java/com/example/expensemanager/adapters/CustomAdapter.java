package com.example.expensemanager.adapters;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.expensemanager.R;
import com.example.expensemanager.models.Alarm;
import com.example.expensemanager.myHandler.ReminderDBHandler;
import com.example.expensemanager.views.activities.AlarmReceiver;
import com.example.expensemanager.views.activities.ReminderActivity;

import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Alarm> alarmList;
    private LayoutInflater layoutInflater;
    public CustomAdapter(Context context, List<Alarm> alarmList) {
        this.context= context;
        this.alarmList= alarmList;
        layoutInflater= (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= layoutInflater.inflate(R.layout.row_reminder, null);
        final Alarm selectedAlarm= alarmList.get(position);
        final TextView nameText= convertView.findViewById(R.id.nameText);
        final TextView alarmText= convertView.findViewById(R.id.timeText);
        final AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);

        nameText.setText(selectedAlarm.getName());
        alarmText.setText(selectedAlarm.getDateString());

        final Intent serviceIntent= new Intent(context, AlarmReceiver.class);
        final Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedAlarm.getYear());
        calendar.set(Calendar.MONTH, selectedAlarm.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, selectedAlarm.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, selectedAlarm.getHour());
        calendar.set(Calendar.MINUTE, selectedAlarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if(calendar.getTimeInMillis() < System.currentTimeMillis()){
            calendar.add(Calendar.DATE, 1);
        }

        ToggleButton toggleButton= convertView.findViewById(R.id.toggle);
        toggleButton.setChecked(selectedAlarm.getStatus());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedAlarm.setStatus(isChecked);
                ReminderDBHandler db= new ReminderDBHandler(context);
                db.updateAlarm(selectedAlarm);

                ReminderActivity.alarmList.clear();
                List<Alarm> list= db.getAllAlarms();
                ReminderActivity.alarmList.addAll(list);
                notifyDataSetChanged();

                if (!isChecked && selectedAlarm.toString().equals(ReminderActivity.activeAlarm)) {
                    serviceIntent.putExtra("extra", "off");
                    PendingIntent pendingIntent= PendingIntent.getBroadcast(context, position, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    context.sendBroadcast(serviceIntent);
                }
            }
        });

        if(selectedAlarm.getStatus()){
            serviceIntent.putExtra("extra", "on");
            serviceIntent.putExtra("active", nameText.getText().toString());
            PendingIntent pendingIntent= PendingIntent.getBroadcast(context, position, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        return convertView;
    }
}
