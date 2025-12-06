package com.example.dormitally.adminfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.dormitally.R;
import com.example.dormitally.Room;
import com.example.dormitally.RoomItemAdapter;
import com.example.dormitally.TenantFullDetails;
import com.example.dormitally.TenantItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class Tenants extends Fragment implements TenantItemAdapter.OnItemActionListener {
    View view;
    TextView totalTenants;
    RecyclerView recyclerView;
    TenantItemAdapter adapter;
    List<Account> accounts = new ArrayList<>();
    DBHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tenants_admin, container, false);
        totalTenants = view.findViewById(R.id.totalTenants);
        recyclerView = view.findViewById(R.id.rvTenants);
        dbHelper = new DBHelper(getContext());
        accounts = dbHelper.getAllAccounts();

        totalTenants.setText(String.valueOf(accounts.size()));
        adapter = new TenantItemAdapter(getContext(),accounts,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onDelete(TenantFullDetails account, int position) {
        int result = dbHelper.deleteAccount(account.getUserId());

        if (result != -1) {
            accounts.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
        }
    }
}