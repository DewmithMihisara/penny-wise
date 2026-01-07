package com.example.expensemanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.CategoryAdapter;
import com.example.expensemanager.databinding.FragmentAddTransBinding;
import com.example.expensemanager.databinding.ListDialogBinding;
import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTransFragment extends BottomSheetDialogFragment {
    public AddTransFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddTransBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentAddTransBinding.inflate(inflater);

        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));

                transaction.setType(Constants.INCOME);
            }
        });
        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));

            transaction.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, year, month, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);

                    transaction.setDate(calendar.getTime());
                });
                datePickerDialog.show();
            }
        });

        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
                AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
                categoryDialog.setView(dialogBinding.getRoot());

                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories,
                        new CategoryAdapter.CategoryClickListener() {
                            @Override
                            public void onCategoryClicked(Category category) {
                                binding.category.setText(category.getCategoryName());
                                transaction.setCategory(category.getCategoryName());
                                categoryDialog.dismiss();
                            }
                        });
                dialogBinding.listRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
                dialogBinding.listRecycler.setAdapter(categoryAdapter);

                categoryDialog.show();
            }
        });


        binding.saveTransBtn.setOnClickListener(c -> {
            String amountText = binding.amount.getText().toString().trim();
            if (amountText.isEmpty()) {
                android.widget.Toast.makeText(getContext(), "Please enter an amount", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    android.widget.Toast.makeText(getContext(), "Amount must be greater than 0", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                android.widget.Toast.makeText(getContext(), "Please enter a valid amount", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            if (transaction.getType() == null || transaction.getType().isEmpty()) {
                android.widget.Toast.makeText(getContext(), "Please select Income or Expense", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            if (transaction.getDate() == null) {
                transaction.setDate(new java.util.Date());
            }

            if (transaction.getCategory() == null || transaction.getCategory().isEmpty()) {
                android.widget.Toast.makeText(getContext(), "Please select a category", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            transaction.setAccount("Cash");

            String note = binding.note.getText().toString();
            if (note == null) {
                note = "";
            }

            if (transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(amount * -1);
            } else {
                transaction.setAmount(amount);
            }

            transaction.setNote(note);

            ((MainActivity) getActivity()).viewModel.addTransactions(transaction);
            ((MainActivity) getActivity()).getTransactions();
            dismiss();
        });

        return binding.getRoot();
    }
}