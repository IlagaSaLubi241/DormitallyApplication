package com.example.dormitally.adminfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitally.DBHelper;
import com.example.dormitally.IssueMessage;
import com.example.dormitally.MessageAdapter;
import com.example.dormitally.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdmin extends Fragment {

    View view;
    List<IssueMessage> messages = new ArrayList<>();
    MessageAdapter adapter;
    RecyclerView recyclerView;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_messages_admin, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMessageAdmin);

        dbHelper = new DBHelper(getContext());

        messages = dbHelper.getAllMessages();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageAdapter(getContext(), messages);
        recyclerView.setAdapter(adapter);
        return view;
    }
}