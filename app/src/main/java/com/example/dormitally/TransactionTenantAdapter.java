package com.example.dormitally;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionTenantAdapter extends RecyclerView.Adapter<TransactionTenantHolder> {
    Context context;
    List<Transaction> list;
    LayoutInflater inflater;
    public TransactionTenantAdapter(Context context, List<Transaction> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public TransactionTenantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionTenantHolder(inflater.inflate(R.layout.transaction_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionTenantHolder holder, int position) {
            Transaction transaction = list.get(position);
            holder.txtMonthYear.setText(transaction.getDate());
            holder.txtAmount.setText(String.valueOf(transaction.getAmount()));
            holder.txtPaidInfo.setText(transaction.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
