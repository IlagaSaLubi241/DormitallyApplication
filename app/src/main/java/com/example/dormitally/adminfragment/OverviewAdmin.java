package com.example.dormitally.adminfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import com.example.dormitally.Account;
import com.example.dormitally.DBHelper;
import com.example.dormitally.PendingApprovalsAdapter;
import com.example.dormitally.R;
import com.example.dormitally.TenantItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class OverviewAdmin extends Fragment {

    View view;
    TextView totalTenants,totalRevenue,numTenants,numMessage,numRoom;
    RecyclerView recyclerView;
    PendingApprovalsAdapter adapter;
    List<Account> accounts = new ArrayList<>();
    DBHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_overview_admin, container, false);
        recyclerView = view.findViewById(R.id.pendingRecyclerView);
        totalTenants = view.findViewById(R.id.totalPending);
        totalRevenue = view.findViewById(R.id.totalRevenue);
        numTenants = view.findViewById(R.id.numTenant);
        numMessage = view.findViewById(R.id.messagesTxt);
        numRoom = view.findViewById(R.id.availRoom);

        dbHelper = new DBHelper(getContext());
        accounts = dbHelper.getAllPendingAccounts();

        numRoom.setText(String.valueOf(dbHelper.getAvailableRoomCount()));
        numTenants.setText(String.valueOf(dbHelper.getActiveTenantCount()));
        totalRevenue.setText(String.valueOf(dbHelper.getTotalCollectedAmount()));
        adapter = new PendingApprovalsAdapter(getContext(),accounts);
        totalTenants.setText(String.valueOf(accounts.size()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}