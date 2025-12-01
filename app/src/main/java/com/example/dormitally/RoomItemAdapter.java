package com.example.dormitally;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomItemAdapter extends RecyclerView.Adapter<RoomItemHolder> {
    private Context context;
    private List<Room> list;
    private LayoutInflater inflater;
    private OnItemActionListener listener;

    public RoomItemAdapter(Context context, List<Room> list, OnItemActionListener listener) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomItemHolder(inflater.inflate(R.layout.room_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomItemHolder holder, int position) {
        Room room = list.get(position);

        holder.Status.setText(room.getRoomStatus());
        holder.RoomName.setText(room.getRoomNum());
        holder.Price.setText(String.valueOf(room.getRoomPrice()));
        holder.PerMonth.setText("per month");
        holder.RoomType.setText(room.getRoomType());
        holder.Capacity.setText("Capacity: " + room.getRoomCapacity() + " person");

        // Correctly handle Delete click
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDelete(room, holder.getAdapterPosition());
                }
            }
        });

        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEdit(room, holder.getAdapterPosition());
                }
            }
        });

        byte[] imageBytes = room.getRoomImage();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.RoomImage.setImageBitmap(bitmap);
        } else {
            holder.RoomImage.setImageResource(R.drawable.ic_people);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemActionListener {
        void onEdit(Room room, int position);   // Open AddRoomPage for editing
        void onDelete(Room room, int position); // Confirm deletion
    }
}
