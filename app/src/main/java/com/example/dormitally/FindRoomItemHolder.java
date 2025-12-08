package com.example.dormitally;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FindRoomItemHolder extends RecyclerView.ViewHolder {
    ImageView roomImage;
    TextView Status;
    TextView RoomName;
    TextView Price;
    TextView RoomType;
    TextView Capacity;
    TextView PerMonth;
    TextView seeDetails;


    public FindRoomItemHolder(@NonNull View itemView) {
        super(itemView);
        roomImage = itemView.findViewById(R.id.imgRoom);
        Status = itemView.findViewById(R.id.txtStatus);
        RoomName = itemView.findViewById(R.id.txtRoomName);
        Price = itemView.findViewById(R.id.txtPrice);
        PerMonth = itemView.findViewById(R.id.txtPerMonth);
        RoomType = itemView.findViewById(R.id.txtRoomType);
        Capacity = itemView.findViewById(R.id.txtCapacity);
        seeDetails = itemView.findViewById(R.id.seeDetailsBtn);
    }
}
