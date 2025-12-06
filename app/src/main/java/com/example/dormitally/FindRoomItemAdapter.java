package com.example.dormitally;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FindRoomItemAdapter extends RecyclerView.Adapter<FindRoomItemHolder> {
    Context context;
    List<Room> list;
    LayoutInflater inflater;

    public FindRoomItemAdapter(Context context, List<Room> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FindRoomItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FindRoomItemHolder(inflater.inflate(R.layout.item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FindRoomItemHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Room room = list.get(pos);

        holder.Status.setText(room.getRoomStatus());
        holder.RoomName.setText(room.getRoomNum());
        holder.Price.setText(String.valueOf(room.getRoomPrice()));
        holder.PerMonth.setText("per month");
        holder.RoomType.setText(room.getRoomType());
        holder.Capacity.setText("Capacity: "+ room.getRoomCapacity()+" person");

        String status = room.getRoomStatus(); // make sure you retrieve it first

        if (status.equalsIgnoreCase("Available")) {
            holder.Status.setBackgroundResource(R.drawable.bg_available);
            holder.Status.setTextColor(Color.parseColor("#079732"));
        } else if (status.equalsIgnoreCase("Occupied")) {
            holder.Status.setBackgroundResource(R.drawable.bg_occupied);
            holder.Status.setTextColor(Color.parseColor("#931C15"));
        } else if (status.equalsIgnoreCase("Maintenance")) {
            holder.Status.setBackgroundResource(R.drawable.bg_maintenance);
            holder.Status.setTextColor(Color.parseColor("#C8B104"));
        }
        holder.seeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos != RecyclerView.NO_POSITION) {
                    Room clickedRoom = list.get(pos);
                    showRoomDetailsDialog(clickedRoom);
                }
            }
        });
      holder.roomImage.setImageURI(room.getRoomImage());


    }
    private void showRoomDetailsDialog(Room room) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View view = inflater.inflate(R.layout.dialog_room_details, null);

        ImageView img = view.findViewById(R.id.imgDetail);
        TextView num = view.findViewById(R.id.tvRoomNum);
        TextView type = view.findViewById(R.id.tvRoomType);
        TextView status = view.findViewById(R.id.tvRoomStatus);
        TextView price = view.findViewById(R.id.tvRoomPrice);
        TextView cap = view.findViewById(R.id.tvRoomCapacity);
        TextView desc = view.findViewById(R.id.tvRoomDescription);
        Button apply = view.findViewById(R.id.btnApply);
        ImageView back = view.findViewById(R.id.btnBack);

        num.setText("Room Number: " + room.getRoomNum());
        type.setText("Type: " + room.getRoomType());
        status.setText("Status: " + room.getRoomStatus());
        price.setText("Price: ₱" + room.getRoomPrice());
        cap.setText("Capacity: " + room.getRoomCapacity() + " person");
        desc.setText("Description: " + room.getRoomDescription());
        img.setImageURI(room.getRoomImage());

        builder.setView(view);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        back.setOnClickListener(v -> dialog.dismiss());

        // APPLY BUTTON CLICK
        apply.setOnClickListener(v -> {
            Intent intent = new Intent(context, RegisterForm.class);
            intent.putExtra("roomId",room.getRoomId());
            context.startActivity(intent);

            dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
