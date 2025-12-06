package com.example.dormitally;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectRoomAdapter extends RecyclerView.Adapter<FindRoomItemHolder> {

    Context context;
    List<Room> list;
    LayoutInflater inflater;
    OnRoomSelectListener listener;

    // ADD LISTENER IN CONSTRUCTOR
    public SelectRoomAdapter(Context context, List<Room> list, OnRoomSelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FindRoomItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FindRoomItemHolder(inflater.inflate(R.layout.item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FindRoomItemHolder holder, int position) {
        Room room = list.get(position);

        holder.Status.setText(room.getRoomStatus());
        holder.RoomName.setText(room.getRoomNum());
        holder.Price.setText(String.valueOf(room.getRoomPrice()));
        holder.PerMonth.setText("per month");
        holder.RoomType.setText(room.getRoomType());
        holder.Capacity.setText("Capacity: "+ room.getRoomCapacity()+" person");
        holder.roomImage.setImageURI(room.getRoomImage());

        // CLICK EVENT
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRoomSelected(room);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // INTERFACE FOR ROOM CLICK
    public interface OnRoomSelectListener {
        void onRoomSelected(Room room);
    }
}

