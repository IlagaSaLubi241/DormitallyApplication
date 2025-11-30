package com.example.dormitally;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.dormitally.tenantfragment.BillsPage;
import com.example.dormitally.tenantfragment.HistoryPage;
import com.example.dormitally.tenantfragment.HomePage;
import com.example.dormitally.tenantfragment.MessagePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TenantDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tenant_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(navListener);

        Fragment selectedFragment = new HomePage();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

    }

    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId(); /* obtain the selected item ID from your source */
                Fragment selectedFragment = null;

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomePage();
                } else if (itemId == R.id.nav_bills) {
                    selectedFragment = new BillsPage();
                } else if (itemId == R.id.nav_history) {
                    // Handle the profile case
                    selectedFragment = new HistoryPage();
                } else if (itemId == R.id.nav_messages) {
                    // Handle the profile case
                    selectedFragment = new MessagePage();
                }else {
                    selectedFragment = new HomePage();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };

}