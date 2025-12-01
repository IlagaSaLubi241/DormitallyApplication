package com.example.dormitally.adminfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.dormitally.AddRoomPage;
import com.example.dormitally.DBHelper;
import com.example.dormitally.R;
import com.example.dormitally.Room;
import com.example.dormitally.RoomItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class Rooms extends Fragment implements View.OnClickListener, RoomItemAdapter.OnItemActionListener {

    private View view;
    private Button addRoombtn;
    private RecyclerView recyclerView;
    private RoomItemAdapter adapter;
    private List<Room> roomsList = new ArrayList<>();
    private DBHelper dbHelper;
    private int editingPosition = -1;
    private boolean isEditing = false;

    private ActivityResultLauncher<Intent> roomLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rooms_admin, container, false);

        addRoombtn = view.findViewById(R.id.btnAddRoom);
        recyclerView = view.findViewById(R.id.recyclerRooms);

        dbHelper = new DBHelper(getContext());
        roomsList = dbHelper.getAllRooms();

        adapter = new RoomItemAdapter(getContext(), roomsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        roomLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        Intent data = result.getData();

                        int roomId = data.getIntExtra("roomId", -1);
                        String roomNumber = data.getStringExtra("roomNumber");
                        String roomType = data.getStringExtra("roomType");
                        String roomStatus = data.getStringExtra("roomStatus");
                        String roomPrice = data.getStringExtra("roomPrice");
                        String roomCapacity = data.getStringExtra("roomCapacity");
                        String roomDescription = data.getStringExtra("roomDescription");
                        byte[] roomImage = data.getByteArrayExtra("roomImage");

                        if (isEditing) {
                            Room updated = new Room(
                                    roomId,
                                    roomNumber,
                                    roomType,
                                    roomStatus,
                                    Double.parseDouble(roomPrice),
                                    Integer.parseInt(roomCapacity),
                                    roomDescription,
                                    roomImage
                            );

                            long ok = dbHelper.updateRoom(updated);
                            if (ok != -1)
                                Toast.makeText(getActivity(), "Room updated", Toast.LENGTH_SHORT).show();

                            roomsList.set(editingPosition, updated);
                            adapter.notifyItemChanged(editingPosition);

                            isEditing = false;

                        } else {
                            Room newRoom = new Room(
                                    0, // auto increment in DB
                                    roomNumber,
                                    roomType,
                                    roomStatus,
                                    Double.parseDouble(roomPrice),
                                    Integer.parseInt(roomCapacity),
                                    roomDescription,
                                    roomImage
                            );

                            long insertedId = dbHelper.addRoom(newRoom);
                            newRoom.setRoomId((int) insertedId);

                            roomsList.add(newRoom);
                            adapter.notifyItemInserted(roomsList.size() - 1);

                            Toast.makeText(getActivity(), "Room added", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        addRoombtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AddRoomPage.class);
        isEditing = false;
        roomLauncher.launch(intent);
    }

    @Override
    public void onEdit(Room room, int position) {
        Intent intent = new Intent(getActivity(), AddRoomPage.class);

        intent.putExtra("roomId", room.getRoomId());
        intent.putExtra("roomNumber", room.getRoomNum());
        intent.putExtra("roomType", room.getRoomType());
        intent.putExtra("roomStatus", room.getRoomStatus());
        intent.putExtra("roomPrice", String.valueOf(room.getRoomPrice()));
        intent.putExtra("roomCapacity", String.valueOf(room.getRoomCapacity()));
        intent.putExtra("roomDescription", room.getRoomDescription());
        intent.putExtra("roomImage", room.getRoomImage());

        isEditing = true;
        editingPosition = position;

        roomLauncher.launch(intent);
    }

    @Override
    public void onDelete(Room room, int position) {
        int result = dbHelper.deleteRoom(room.getRoomId());

        if (result != -1) {
            roomsList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getActivity(), "Room deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
