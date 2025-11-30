package com.example.dormitally;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.dormitally.adminfragment.BillsAdmin;
import com.example.dormitally.adminfragment.MessagesAdmin;
import com.example.dormitally.adminfragment.OverviewAdmin;
import com.example.dormitally.adminfragment.ReportsAdmin;
import com.example.dormitally.adminfragment.Rooms;
import com.example.dormitally.adminfragment.Tenants;
import com.example.dormitally.tenantfragment.BillsPage;
import com.example.dormitally.tenantfragment.HistoryPage;
import com.example.dormitally.tenantfragment.HomePage;
import com.example.dormitally.tenantfragment.MessagePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_overview);
        bottomNav.setOnItemSelectedListener(navListener);

        Fragment selectedFragment = new OverviewAdmin();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

    }
    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId(); /* obtain the selected item ID from your source */
                Fragment selectedFragment = null;

                if (itemId == R.id.nav_overview) {
                    selectedFragment = new OverviewAdmin();
                } else if (itemId == R.id.nav_tenants) {
                    selectedFragment = new Tenants();
                } else if (itemId == R.id.nav_rooms) {
                    selectedFragment = new Rooms();
                } else if (itemId == R.id.nav_bills) {
                    selectedFragment = new BillsAdmin();
                }else if (itemId == R.id.nav_messages) {
                    // Handle the profile case
                    selectedFragment = new MessagesAdmin();
                }else if (itemId == R.id.nav_reports) {
                    // Handle the profile case
                    selectedFragment = new ReportsAdmin();
                }
                else {
                    selectedFragment = new HomePage();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };

}