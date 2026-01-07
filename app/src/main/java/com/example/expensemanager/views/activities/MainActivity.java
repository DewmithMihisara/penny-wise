package com.example.expensemanager.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.TransAdapter;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.viewmodels.MainViewModel;
import com.example.expensemanager.views.fragments.AddStatsFragment;
import com.example.expensemanager.views.fragments.AddTransFragment;
import com.example.expensemanager.views.fragments.NotesFragment;
import com.example.expensemanager.views.fragments.SummaryFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    SummaryFragment summaryFragment= (SummaryFragment) getSupportFragmentManager().findFragmentByTag("summaryFragment");
    NotesFragment notesFragment= (NotesFragment) getSupportFragmentManager().findFragmentByTag("notesFragment");
    AddStatsFragment statsFragment = (AddStatsFragment) getSupportFragmentManager().findFragmentByTag("statsFragment");
    ActivityMainBinding binding;
    Calendar calendar;
    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel= new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDate.setOnClickListener(c->{
            if(Constants.SELECTED_TAB==Constants.DAILY){
                calendar.add(Calendar.DATE, 1);
            }
            else if(Constants.SELECTED_TAB==Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.prevDate.setOnClickListener(c->{
            if(Constants.SELECTED_TAB==Constants.DAILY){
                calendar.add(Calendar.DATE, -1);
            }
            else if(Constants.SELECTED_TAB==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,-1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddTransFragment().show(getSupportFragmentManager(), null);
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getText().equals("Monthly")){
                    binding.constraintContainer.setVisibility(View.VISIBLE);

                    if (summaryFragment != null) {
                        remFrag(summaryFragment);
                    }
                    if (notesFragment != null) {
                        remFrag(notesFragment);
                    }

                    Constants.SELECTED_TAB=1;
                    updateDate();
                }
                else if (tab.getText().equals("Daily")) {
                    if(summaryFragment != null){
                        binding.constraintContainer.setVisibility(View.VISIBLE);
                        remFrag(summaryFragment);
                    }
                    if (notesFragment != null) {
                        binding.constraintContainer.setVisibility(View.VISIBLE);
                        remFrag(notesFragment);
                    }

                    Constants.SELECTED_TAB=0;
                    calendar = Calendar.getInstance();
                    updateDate();
                }
                else if(tab.getText().equals("Summary")){
                    binding.constraintContainer.setVisibility(View.GONE);

                    if(notesFragment != null){
                        remFrag(notesFragment);
                    }

                    summaryFragment = new SummaryFragment();
                    loadTabFragment(summaryFragment, "summaryFragment");

                    Constants.SELECTED_TAB=3;
                    updateDate();
                }
                else if (tab.getText().equals("Notes")) {
                    binding.constraintContainer.setVisibility(View.GONE);

                    if(summaryFragment != null){
                        remFrag(summaryFragment);
                    }
                    notesFragment= new NotesFragment();
                    loadTabFragment(notesFragment, "notesFragment");

                    Constants.SELECTED_TAB=4;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.transList.setLayoutManager(new LinearLayoutManager(this));
        viewModel.transactionMutableLiveData.observe(this, new Observer<ArrayList<Transaction>>() {
            @Override
            public void onChanged(ArrayList<Transaction> transactions) {
                TransAdapter transAdapter= new TransAdapter(MainActivity.this, transactions);
                binding.transList.setAdapter(transAdapter);
                if(transactions.size()>0){
                    binding.emptyState.setVisibility(View.GONE);
                }
                else{
                    binding.emptyState.setVisibility(View.VISIBLE);
                }

            }
        });

        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);

        binding.bottomNavigation.setOnItemSelectedListener(item-> {
            if (item.getItemId() == R.id.trans) {
                binding.constraintWrap.setVisibility(View.VISIBLE);

                if (statsFragment != null) {
                    remFrag(statsFragment);
                }
                
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Transactions");
                }
            }
            else if (item.getItemId() == R.id.stats) {
                binding.constraintWrap.setVisibility(View.GONE);

                statsFragment= new AddStatsFragment();
                loadFrag(statsFragment, "statsFragment");
                
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Stats for last 6 months");
                }
            }
            else if (item.getItemId() == R.id.more) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("More");
                String[] options = {"Privacy Policy", "About"};
                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                    } else if (which == 1) {
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    }
                });
                builder.show();
            }

            return true;
        });

    }

    public void loadTabFragment(Fragment fragment,String tag){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.MainContainer, fragment,tag);
        ft.commit();
    }

    public void loadFrag(Fragment fragment, String tag){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.MainContainerWrapper, fragment, tag);
        ft.commit();
    }

    public void remFrag(Fragment fragment){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment).commit();
    }

    public void getTransactions(){
        viewModel.getTransactions(calendar);
    }

    void updateDate(){
        if(Constants.SELECTED_TAB==Constants.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
            viewModel.getTransactions(calendar);
        }
        else if(Constants.SELECTED_TAB==Constants.MONTHLY){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
            viewModel.getTransactions(calendar);
        }
        else if(Constants.SELECTED_TAB==Constants.SUMMARY){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.reminder) {
            startActivity(new Intent(MainActivity.this, ReminderActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}