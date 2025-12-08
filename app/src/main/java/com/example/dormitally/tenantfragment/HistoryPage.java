package com.example.dormitally.tenantfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitally.Balance;
import com.example.dormitally.DBHelper;
import com.example.dormitally.R;
import com.example.dormitally.Transaction;
import com.example.dormitally.TransactionTenantAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryPage extends Fragment {


    View view;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    TransactionTenantAdapter transactionTenantAdapter;
    int accountId;
    List<Transaction> transactions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_history_tenant,container, false);
        if (getArguments() != null) {
            accountId = getArguments().getInt("userId", -1);
        }
        recyclerView =view.findViewById(R.id.recyclerBills);
        dbHelper = new DBHelper(getContext());
        transactions = dbHelper.getTransactionsByUserId(accountId);

        transactionTenantAdapter = new TransactionTenantAdapter(getContext(),transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(transactionTenantAdapter);
        return view;
    }
}