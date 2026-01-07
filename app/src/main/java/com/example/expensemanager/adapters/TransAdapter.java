package com.example.expensemanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowTransactionBinding;
import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.views.activities.MainActivity;

import java.util.ArrayList;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.TransViewHolder> {

    Context context;
    ArrayList<Transaction> transList;
    public TransAdapter(Context context, ArrayList<Transaction> transList){
        this.context= context;
        this.transList= transList;
    }
    @NonNull
    @Override
    public TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransViewHolder holder, int position) {
        Transaction transaction= transList.get(position);
        holder.binding.transAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.transDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transCategory.setText(transaction.getCategory());

        Category transCategory = Constants.getCategorieDetails(transaction.getCategory());
        if (transCategory != null) {
            holder.binding.catIcon.setImageResource(transCategory.getCategoryImg());
            holder.binding.catIcon.setBackgroundTintList(context.getColorStateList(transCategory.getCategoryColor()));
        }

        if (transaction.getType() != null) {
            if (transaction.getType().equals(Constants.INCOME)) {
                holder.binding.transAmount.setTextColor(context.getColor(R.color.green));
            } else if (transaction.getType().equals(Constants.EXPENSE)) {
                holder.binding.transAmount.setTextColor(context.getColor(R.color.red));
            }
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog deleteDialog= new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are you sure to delete this transaction?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)context).viewModel.deleteTransaction(transaction);
                    }
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDialog.dismiss();
                    }
                });
                deleteDialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transList.size();
    }

    public class TransViewHolder extends RecyclerView.ViewHolder{

        RowTransactionBinding binding;

        public TransViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= RowTransactionBinding.bind(itemView);
        }
    }
}
