package com.example.dormitally;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TenantBillAdapter extends RecyclerView.Adapter<TenantBillHolder> {

    Context context;
    List<Balance> list;
    LayoutInflater inflater;

    public interface OnItemActionListener {
        void onPayClick(Balance balance);
        void onItemClick(Balance balance);
    }

    OnItemActionListener listener;

    public TenantBillAdapter(
            Context context,
            List<Balance> list,
            OnItemActionListener listener
    ) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TenantBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TenantBillHolder(inflater.inflate(R.layout.bill_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TenantBillHolder holder, int position) {
        Balance b = list.get(position);
        DBHelper dbHelper = new DBHelper(context);
        TenantBillDetails tb = dbHelper.getTenantBillDetailsByBalanceId(b.getBalanceId());
        String paidDate = tb.getPaidDate();
        String paymentMethod = tb.getPaymentMethod();

        holder.txtAmount.setText("₱" + b.getTotalBalance());
        holder.txtDueDate.setText(b.getDueDate());
        holder.txtStatus.setText(b.getBalanceStatus());
        holder.txtVerification.setText(b.getVerification());
        holder.txtPaidInfo.setText("Paid on " + paidDate + " via " + paymentMethod);

        String status = b.getBalanceStatus();
        String verification = b.getVerification();

        // Reset visibility first (important for RecyclerView reuse)
        holder.paidRow.setVisibility(View.GONE);
        holder.dueDateRow.setVisibility(View.GONE);
        holder.btnPay.setVisibility(View.GONE);

        // ----------------------------
        // CONDITIONS
        // ----------------------------
        if (status.equalsIgnoreCase("Pending") && verification.equalsIgnoreCase("Unverified")) {
            // SHOW pay button + show due date
            holder.btnPay.setVisibility(View.VISIBLE);
            holder.dueDateRow.setVisibility(View.VISIBLE);
            holder.paidRow.setVisibility(View.GONE);

        } else if (status.equalsIgnoreCase("Paid") && verification.equalsIgnoreCase("Unverified")) {
            // Paid but still unverified → can't pay again
            holder.btnPay.setVisibility(View.GONE);
            holder.dueDateRow.setVisibility(View.VISIBLE);
            holder.paidRow.setVisibility(View.GONE);

        } else if (status.equalsIgnoreCase("Paid") && verification.equalsIgnoreCase("Verified")) {
            // Fully paid and verified → show completed payment row
            holder.btnPay.setVisibility(View.GONE);
            holder.dueDateRow.setVisibility(View.GONE);
            holder.paidRow.setVisibility(View.VISIBLE);
        }

        // Handle click on entire item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(b);
        });

        // Handle Pay button click
        holder.btnPay.setOnClickListener(v -> {
            if (listener != null) listener.onPayClick(b);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
