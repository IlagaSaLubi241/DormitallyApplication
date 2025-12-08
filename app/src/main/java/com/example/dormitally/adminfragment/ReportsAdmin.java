package com.example.dormitally.adminfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.dormitally.DBHelper;
import com.example.dormitally.R;
import com.example.dormitally.Transaction;
import com.example.dormitally.TransactionTenantAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReportsAdmin extends Fragment {

    View view;
    TextView txtOccupiedRooms, txtOccupancyRate, txtActiveTenants, txtTotalPayments,txtRevenue;
    Button btnDownload;
    RecyclerView transactionRecycler;
    DBHelper dbHelper;
    TransactionTenantAdapter transactionAdapter;
    List<Transaction> transactions = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_reports_admin, container, false);
        transactionRecycler =view.findViewById(R.id.recyclerTransaction);
        txtOccupiedRooms = view.findViewById(R.id.txtOccupiedRooms);
        txtOccupancyRate = view.findViewById(R.id.txtOccupancyRate);
        txtActiveTenants = view.findViewById(R.id.txtActiveTenants);
        txtTotalPayments = view.findViewById(R.id.txtTotalPayments);
        btnDownload = view.findViewById(R.id.btnDownload);
        txtRevenue = view.findViewById(R.id.txtRevenue);
        dbHelper = new DBHelper(getContext());

        txtRevenue.setText(String.valueOf(dbHelper.getTotalCollectedAmount()));
        txtOccupiedRooms.setText(dbHelper.getOccupiedRoomsFraction());
        txtOccupancyRate.setText(dbHelper.getOccupancyRate());
        txtActiveTenants.setText(String.valueOf(dbHelper.numOfActiveTenants()));
        txtTotalPayments.setText(String.valueOf(dbHelper.getTotalTransactionsCount()));

        transactions = dbHelper.getAllTransactions();
        transactionAdapter = new TransactionTenantAdapter(getContext(),transactions);
        transactionRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionRecycler.setAdapter(transactionAdapter);
        return view;
    }
}