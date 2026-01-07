package com.example.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowAccBinding;
import com.example.expensemanager.models.Account;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder>{

    Context context;
    ArrayList<Account> accountArrayList;

    public interface AccountClickListener{
        void onAccountSelected(Account account);
    }

    AccountClickListener accountClickListener;
    public AccountAdapter(Context context, ArrayList<Account> accountArrayList, AccountClickListener accountClickListener){
        this.context = context;
        this.accountArrayList = accountArrayList;
        this.accountClickListener= accountClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountViewHolder(LayoutInflater.from(context).inflate(R.layout.row_acc, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account account = accountArrayList.get(position);
        holder.binding.accountName.setText(account.getAccName());
        holder.itemView.setOnClickListener(c->{
            accountClickListener.onAccountSelected(account);
        });
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{

        RowAccBinding binding;

        public AccountViewHolder(View itemView) {
            super(itemView);
            binding = RowAccBinding.bind(itemView);
        }
    }
}
