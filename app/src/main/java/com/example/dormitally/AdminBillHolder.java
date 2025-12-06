package com.example.dormitally;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminBillHolder extends RecyclerView.ViewHolder {
    TextView name, amount, dateRoom, room, paidStatus, verificationStatus, paidDate, methodPayment;
    Button btnViewDetails, btnVerifyPayment;
    ImageView btnEdit, btnDelete;

    public AdminBillHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tvName);
        amount = itemView.findViewById(R.id.tvAmount);
        dateRoom = itemView.findViewById(R.id.tvDateRoom);
        room = itemView.findViewById(R.id.tvRoom);
        paidStatus = itemView.findViewById(R.id.tvPaidStatus);
        verificationStatus = itemView.findViewById(R.id.tvVerificationStatus);
        paidDate = itemView.findViewById(R.id.paidDate);
        methodPayment = itemView.findViewById(R.id.methodPayment);
        btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        btnVerifyPayment = itemView.findViewById(R.id.btnVerifyPayment);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}
