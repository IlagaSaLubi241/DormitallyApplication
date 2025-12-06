package com.example.dormitally;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoomItemHolder extends RecyclerView.ViewHolder {
    TextView Status;
    TextView RoomName;
    TextView Price;
    TextView PerMonth;
    TextView RoomType;
    TextView Capacity;
    ImageView RoomImage,Edit,Delete;

    public RoomItemHolder(@NonNull View itemView) {
        super(itemView);
        Status = itemView.findViewById(R.id.txtStatus);
        RoomName = itemView.findViewById(R.id.txtRoomName);
        Price = itemView.findViewById(R.id.txtPrice);
        PerMonth = itemView.findViewById(R.id.txtPerMonth);
        RoomType = itemView.findViewById(R.id.txtRoomType);
        Capacity = itemView.findViewById(R.id.txtCapacity);
        Edit = itemView.findViewById(R.id.btnEdit);
        RoomImage = itemView.findViewById(R.id.imgRoom);
        Delete = itemView.findViewById(R.id.btnDelete);
    }
}
