package com.example.dormitally;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TenantBillHolder extends RecyclerView.ViewHolder {

    TextView txtMonthYear, txtAmount, txtPaidInfo, txtDueDate, txtStatus,txtVerification;
    LinearLayout paidRow, dueDateRow,itemLayout;
    Button btnPay;

    public TenantBillHolder(@NonNull View itemView) {
        super(itemView);

        txtMonthYear = itemView.findViewById(R.id.txtMonthYear);
        txtAmount = itemView.findViewById(R.id.txtAmount);
        txtPaidInfo = itemView.findViewById(R.id.txtPaidInfo);
        txtDueDate = itemView.findViewById(R.id.dueDateText);
        txtStatus = itemView.findViewById(R.id.statusTextView);
        paidRow = itemView.findViewById(R.id.paidRow);
        dueDateRow = itemView.findViewById(R.id.dueDateRow);
        itemLayout = itemView.findViewById(R.id.itemLayout);
        btnPay = itemView.findViewById(R.id.btnPaynow);
        txtVerification = itemView.findViewById(R.id.verificationTextView);
    }
}
