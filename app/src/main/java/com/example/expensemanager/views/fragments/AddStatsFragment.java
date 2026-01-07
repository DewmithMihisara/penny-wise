package com.example.expensemanager.views.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentAddStatsBinding;
import com.example.expensemanager.viewmodels.MainViewModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class AddStatsFragment extends Fragment {

    FragmentAddStatsBinding binding;
    Calendar calendar;
    public MainViewModel viewModel;

    public AddStatsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentAddStatsBinding.inflate(inflater);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ArrayList<Integer> incomeList = viewModel.getIncomeForLast6Months();
        ArrayList<Integer> expenseList = viewModel.getExpenseForLast6Months();

        if (incomeList == null || incomeList.isEmpty()) {
            incomeList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                incomeList.add(0);
            }
        }

        if (expenseList == null || expenseList.isEmpty()) {
            expenseList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                expenseList.add(0);
            }
        }

        while (incomeList.size() < 6) {
            incomeList.add(0);
        }
        while (expenseList.size() < 6) {
            expenseList.add(0);
        }

        ArrayList<BarEntry> incomeEntries = new ArrayList<>();
        ArrayList<BarEntry> expenseEntries = new ArrayList<>();
        ArrayList<String> monthList = new ArrayList<>();

        calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String[] monthNames = dateFormatSymbols.getShortMonths();

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        tempCalendar.add(Calendar.MONTH, -5);

        for (int i = 0; i < 6; i++) {
            int monthIndex = tempCalendar.get(Calendar.MONTH);
            int year = tempCalendar.get(Calendar.YEAR);

            String monthName = monthNames[monthIndex];

            if (year != currentYear) {
                monthName = monthName + " " + (year % 100);
            }

            monthList.add(monthName);
            incomeEntries.add(new BarEntry(i, incomeList.get(i)));
            expenseEntries.add(new BarEntry(i, expenseList.get(i)));

            tempCalendar.add(Calendar.MONTH, 1);
        }

        if (binding.barChart != null && getContext() != null) {
            BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "Income");
            incomeDataSet.setColor(ContextCompat.getColor(getContext(), R.color.success));
            incomeDataSet.setValueTextColor(Color.BLACK);
            incomeDataSet.setValueTextSize(12f);

            BarDataSet expenseDataSet = new BarDataSet(expenseEntries, "Expense");
            expenseDataSet.setColor(ContextCompat.getColor(getContext(), R.color.error));
            expenseDataSet.setValueTextColor(Color.BLACK);
            expenseDataSet.setValueTextSize(12f);

            BarData barData = new BarData(incomeDataSet, expenseDataSet);

            float groupSpace = 0.4f;
            float barSpace = 0.05f;
            float barWidth = 0.2f;

            barData.setBarWidth(barWidth);

            binding.barChart.setData(barData);
            binding.barChart.groupBars(0, groupSpace, barSpace);

            binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(monthList));
            binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            binding.barChart.getXAxis().setDrawGridLines(false);
            binding.barChart.getXAxis().setGranularity(1f);
            binding.barChart.getXAxis().setLabelCount(monthList.size());
            binding.barChart.getXAxis().setLabelRotationAngle(-45);
            binding.barChart.getXAxis().setTextSize(12f);

            binding.barChart.getAxisLeft().setTextSize(12f);
            binding.barChart.getAxisLeft().setDrawGridLines(true);
            binding.barChart.getAxisRight().setEnabled(false);

            binding.barChart.getLegend().setEnabled(true);
            binding.barChart.getLegend().setTextSize(12f);

            binding.barChart.getDescription().setEnabled(false);

            binding.barChart.animateY(1000);
            binding.barChart.invalidate();

            binding.barChart.setMinimumWidth(600);
        }

        return binding.getRoot();
    }
}