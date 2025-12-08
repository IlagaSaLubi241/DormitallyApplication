package com.example.dormitally;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageHolder extends RecyclerView.ViewHolder {
    TextView titleTxt,dateTxt,senderTxt,messageTxt;
    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        titleTxt = itemView.findViewById(R.id.txtIssueTitle);
        dateTxt = itemView.findViewById(R.id.txtIssueDate);
        senderTxt = itemView.findViewById(R.id.txtIssueSender);
        messageTxt = itemView.findViewById(R.id.txtIssueMessage);
    }
}
