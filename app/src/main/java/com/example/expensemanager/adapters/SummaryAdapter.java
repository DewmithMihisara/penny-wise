package com.example.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowCategoryBinding;
import com.example.expensemanager.models.Summary;

import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    Context context;
    ArrayList<Summary> summaryList;

    public SummaryAdapter(Context context, ArrayList<Summary> summaryList) {
        this.context= context;
        this.summaryList= summaryList;
    }
    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SummaryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        Summary summary= summaryList.get(position);
        holder.binding.catType.setText(summary.getCategoryType());
        holder.binding.amt.setText(String.valueOf(summary.getAmount()));
        holder.binding.amtPer.setText(String.valueOf(summary.getPerAmount()));
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder{

        RowCategoryBinding binding;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= RowCategoryBinding.bind(itemView);
        }
    }
}
