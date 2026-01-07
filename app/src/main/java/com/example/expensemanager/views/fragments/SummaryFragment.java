package com.example.expensemanager.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.expensemanager.databinding.FragmentSummaryBinding;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.viewmodels.MainViewModel;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SummaryFragment extends Fragment {

    FragmentSummaryBinding binding;
    MainViewModel viewModel;
    Calendar calendar;
    public SummaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        binding= FragmentSummaryBinding.inflate(inflater);
        Toast.makeText(getActivity(), "Summary Fragment", Toast.LENGTH_SHORT).show();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ArrayList<PieEntry> entries= new ArrayList<>();

        HashMap<String, Double> map= viewModel.getCategoryPercentage(calendar);

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            float floatValue = value.floatValue();
            Log.d("abc",key + ": " + Helper.formatDouble(floatValue));
            entries.add(new PieEntry(floatValue, key));
        }

        PieDataSet pieDataSet= new PieDataSet(entries, "Subjects");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(20f);

        pieDataSet.setValueFormatter(new PercentValueFormatter());

        PieData pieData= new PieData(pieDataSet);
        binding.pieChart.setData(pieData);

        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.animateY(1000);
        binding.pieChart.invalidate();

        return binding.getRoot();
    }
    public class PercentValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.format("%.1f%%", value);
        }
    }

}