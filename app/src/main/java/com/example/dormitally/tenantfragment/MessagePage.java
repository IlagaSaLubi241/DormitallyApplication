package com.example.dormitally.tenantfragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.dormitally.DBHelper;
import com.example.dormitally.IssueMessage;
import com.example.dormitally.MessageAdapter;
import com.example.dormitally.R;

import java.util.ArrayList;
import java.util.List;

public class MessagePage extends Fragment {

    private EditText subjectEt, messageEt;
    private Button sendBtn;
    private RecyclerView recyclerView;

    private List<IssueMessage> messages = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private DBHelper dbHelper;

    private int accountId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message_tenant, container, false);

        // Read user ID from arguments
        if (getArguments() != null) {
            accountId = getArguments().getInt("userId", -1);
        }

        // Initialize UI components
        subjectEt = view.findViewById(R.id.etSubject);
        messageEt = view.findViewById(R.id.etMessage);
        sendBtn = view.findViewById(R.id.btnSendMessage);
        recyclerView = view.findViewById(R.id.recyclerMessageTenant);

        dbHelper = new DBHelper(getContext());

        // Load messages of tenant (sent + received)
        messages = dbHelper.getMessagesByUserId(accountId);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessageAdapter(getContext(), messages);
        recyclerView.setAdapter(messageAdapter);

        sendBtn.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        String subject = subjectEt.getText().toString().trim();
        String body = messageEt.getText().toString().trim();

        if (subject.isEmpty() || body.isEmpty()) {
            Toast.makeText(getContext(), "Please enter subject and message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate current date/time
        String dateToday;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            dateToday = now.format(formatter);
        } else {
            dateToday = String.valueOf(System.currentTimeMillis());
        }

        // Admin receiver ID (set properly)
        int receiverId = 100;

        // Create new message (using correct constructor)
        IssueMessage message = new IssueMessage(
                accountId,          // senderId
                receiverId,         // receiverId
                body,               // message text
                dateToday,          // timestamp
                "unread",           // status
                subject             // title
        );

        // Insert into database
        long result = dbHelper.addMessage(message);

        if (result != -1) {
            Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();

            // Clear input fields
            subjectEt.setText("");
            messageEt.setText("");

            // Reload messages from database to ensure correct IDs
            messages = dbHelper.getMessagesByUserId(accountId);

            // Update adapter
            messageAdapter.updateList(messages);

        } else {
            Toast.makeText(getContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }
}
