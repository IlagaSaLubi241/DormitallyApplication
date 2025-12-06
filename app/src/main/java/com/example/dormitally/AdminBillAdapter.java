package com.example.dormitally;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminBillAdapter extends RecyclerView.Adapter<AdminBillHolder> {

    Context context;
    List<TenantBillDetails> list;
    LayoutInflater inflater;
    OnItemActionListener listener;

    public AdminBillAdapter(Context context, List<TenantBillDetails> list, OnItemActionListener listener) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminBillHolder(inflater.inflate(R.layout.admin_item_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBillHolder holder, int position) {
        TenantBillDetails tb = list.get(position);
        String status = tb.getBalanceStatus();          // Pending / Paid / Verified
        String verification = tb.getVerification();     // Not Verified / Verified

        // Basic info
        holder.name.setText(tb.getFullName());
        holder.room.setText("Room: " + tb.getRoomNum());
        holder.dateRoom.setText(tb.getDate());
        holder.amount.setText("₱" + tb.getTotalBalance());
        holder.paidStatus.setText(tb.getBalanceStatus());
        holder.verificationStatus.setText(tb.getVerification());
        holder.paidDate.setText(tb.getPaidDate());
        holder.methodPayment.setText(tb.getPaymentMethod());

        // -------------------------------
        // RESET ALL VISIBILITY FIRST
        // -------------------------------
        holder.btnViewDetails.setVisibility(View.GONE);
        holder.btnVerifyPayment.setVisibility(View.GONE);
        holder.btnEdit.setVisibility(View.GONE);
        holder.btnDelete.setVisibility(View.GONE);
        holder.paidDate.setVisibility(View.GONE);
        holder.methodPayment.setVisibility(View.GONE);

        // -------------------------------
        // STATUS LOGIC
        // -------------------------------

        // 1️⃣ CASE: PENDING (not paid)
        if (status.equalsIgnoreCase("Pending" ) && verification.equalsIgnoreCase("Not Verified")) {
            holder.paidStatus.setBackgroundResource(R.drawable.bg_maintenance);
            holder.paidStatus.setTextColor(Color.parseColor("#C8B104"));
            holder.btnViewDetails.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            // hide paid details
            holder.paidDate.setVisibility(View.GONE);
            holder.methodPayment.setVisibility(View.GONE);
        }

        // 2️⃣ CASE: PAID but NOT VERIFIED yet
        else if (status.equalsIgnoreCase("Paid") &&
                verification.equalsIgnoreCase("Not Verified")) {

            holder.btnViewDetails.setVisibility(View.VISIBLE);
            holder.btnVerifyPayment.setVisibility(View.VISIBLE);  // verify button appears
            holder.btnDelete.setVisibility(View.VISIBLE);

            // show payment details
            holder.paidDate.setVisibility(View.VISIBLE);
            holder.methodPayment.setVisibility(View.VISIBLE);
        }

        // 3️⃣ CASE: VERIFIED (completed)
        else if (status.equalsIgnoreCase("Paid") &&
                verification.equalsIgnoreCase("Verified")) {
            holder.paidStatus.setBackgroundResource(R.drawable.bg_available);
            holder.verificationStatus.setBackgroundResource(R.drawable.bg_available);
            holder.paidStatus.setTextColor(Color.parseColor("#079732"));
            holder.verificationStatus.setTextColor(Color.parseColor("#079732"));

            holder.btnViewDetails.setVisibility(View.VISIBLE);

            // show payment info
            holder.paidDate.setVisibility(View.VISIBLE);
            holder.methodPayment.setVisibility(View.VISIBLE);

            // no edit / delete allowed
        }

        // 4️⃣ OPTIONAL CASE: Overdue
        else if(status.equalsIgnoreCase("Overdue")){
            holder.btnViewDetails.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        // ---------------------------------
        // BUTTON CLICK LISTENERS
        // ---------------------------------
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(tb, position));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(tb, position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemActionListener {
        void onEdit(TenantBillDetails balance, int position);
        void onDelete(TenantBillDetails balance, int position);

    }
}
