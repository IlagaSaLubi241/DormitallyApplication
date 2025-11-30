package com.example.dormitally;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomItemAdapter extends RecyclerView.Adapter<RoomItemHolder> {
    Context context;
    List<Room> list;
    LayoutInflater inflater;

    public RoomItemAdapter(Context context, List<Room> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RoomItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomItemHolder(inflater.inflate(R.layout.room_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomItemHolder holder, int position) {
        holder.Status.setText(list.get(position).getRoomStatus());
        holder.RoomName.setText(list.get(position).getRoomNum());
        holder.Price.setText(String.valueOf(list.get(position).getRoomPrice()));
        holder.PerMonth.setText("per month");
        holder.RoomType.setText(list.get(position).getRoomType());
        holder.Capacity.setText("Capacity: "+list.get(position).getRoomCapacity()+" person");
        holder.Manage.setOnClickListener(v -> {
        });
        holder.Edit.setOnClickListener(v -> {
        });
        byte[] imageBytes = list.get(position).getRoomImage();

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
}
