package com.example.dormitally.adminfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitally.AddRoomPage;
import com.example.dormitally.DBHelper;
import com.example.dormitally.R;
import com.example.dormitally.Room;
import com.example.dormitally.RoomItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class Rooms extends Fragment implements View.OnClickListener {
    View view;
    Button addRoombtn;
    RecyclerView recyclerView;
    RoomItemAdapter adapter;
    List<Room> roomsList = new ArrayList<>();
    DBHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_rooms_admin, container, false);
        addRoombtn = view.findViewById(R.id.btnAddRoom);
        recyclerView = view.findViewById(R.id.recyclerRooms);

        dbHelper = new DBHelper(getContext());
        roomsList = dbHelper.getAllRooms();

        adapter = new RoomItemAdapter(getContext(),roomsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        addRoombtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        if(id == R.id.btnAddRoom) {
            intent = new Intent(getActivity(), AddRoomPage.class);
            startActivity(intent);
        }
    }
}