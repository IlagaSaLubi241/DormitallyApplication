package com.example.dormitally;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dormitally.tenantfragment.BillsPage;
import com.example.dormitally.tenantfragment.HistoryPage;
import com.example.dormitally.tenantfragment.HomePage;
import com.example.dormitally.tenantfragment.MessagePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TenantDashboard extends AppCompatActivity {

    private int userId; // <-- Store the tenant ID coming from login
    DBHelper dbHelper;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_dashboard);
        name = findViewById(R.id.tenant_Name);
        // ✅ Get userId from MainActivity login
        userId = getIntent().getIntExtra("userId", -1);
        dbHelper = new DBHelper(this);
        String name = dbHelper.getFullNameByUserId(userId);

        this.name.setText(name);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;

            if (item.getItemId() == R.id.nav_home) {
                fragment = new HomePage();
            } else if (item.getItemId() == R.id.nav_bills) {
                fragment = new BillsPage();
            } else if (item.getItemId() == R.id.nav_history) {
                fragment = new HistoryPage();
            } else if (item.getItemId() == R.id.nav_messages) {
                fragment = new MessagePage();
            } else {
                fragment = new HomePage();
            }

            loadFragmentWithUserId(fragment);
            return true;
        });

        // Load default fragment with userId
        loadFragmentWithUserId(new HomePage());
    }

    // ⭐ Pass userId to any fragment
    private void loadFragmentWithUserId(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
