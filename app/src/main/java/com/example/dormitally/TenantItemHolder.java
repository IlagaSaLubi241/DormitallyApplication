package com.example.dormitally;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TenantItemHolder extends RecyclerView.ViewHolder {
    TextView nameTxt,statusTxt,emailTxt,roomTxt,balanceTxt;
    ImageView btnDelete;
    Button btnViewDetails,btnApprove;

    public TenantItemHolder(@NonNull View itemView) {
        super(itemView);
        nameTxt = itemView.findViewById(R.id.txtName);
        statusTxt = itemView.findViewById(R.id.txtStatus);
        emailTxt = itemView.findViewById(R.id.txtEmail);
        roomTxt = itemView.findViewById(R.id.txtRoom);
        balanceTxt = itemView.findViewById(R.id.txtBalance);
        btnDelete = itemView.findViewById(R.id.btnDelete);
        btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        btnApprove = itemView.findViewById(R.id.btnApprove);
    }
}
