package com.example.dormitally;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PendingApprovalsHolder extends RecyclerView.ViewHolder {
    TextView name,occupation,review;

    public PendingApprovalsHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nameTxt);
        occupation = itemView.findViewById(R.id.occupationTxt);
        review = itemView.findViewById(R.id.reviewBtn);

    }
}
