package com.example.dormitally;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FindRoomPage extends AppCompatActivity implements View.OnClickListener {
    ImageView bckBtn;
    RecyclerView recyclerView;
    FindRoomItemAdapter adapter;
    List<Room> roomsList = new ArrayList<>();
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_room_page);

        bckBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.recyclerViewFindRoom);

        dbHelper = new DBHelper(this);
        roomsList = dbHelper.getAllRooms();

        adapter = new FindRoomItemAdapter(this,roomsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        bckBtn.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        if(id == R.id.backBtn){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}