package com.example.dormitally;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionTenantHolder extends RecyclerView.ViewHolder {
    TextView txtMonthYear;
    TextView txtAmount;
    TextView txtPaidInfo;

    public TransactionTenantHolder(@NonNull View itemView) {
        super(itemView);
        txtMonthYear = itemView.findViewById(R.id.txtMonthYear);
        txtAmount = itemView.findViewById(R.id.txtAmount);
        txtPaidInfo = itemView.findViewById(R.id.txtPaidInfo);
    }
}
