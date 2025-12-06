package com.example.dormitally;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button tenantBtn, adminBtn, loginBtn;
    TextView createAccountBtn, accountType;
    EditText userTxt, passwordTxt;
    LinearLayout findRoomBtn;
    String accountSelected = "tenant"; // default
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        accountType = findViewById(R.id.accountType);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        tenantBtn = findViewById(R.id.tenantBtn);
        adminBtn = findViewById(R.id.adminBtn);
        findRoomBtn = findViewById(R.id.findRoomBtn);
        loginBtn = findViewById(R.id.loginBtn);
        userTxt = findViewById(R.id.emailEditTxt);
        passwordTxt = findViewById(R.id.passwordEditTxt);

        createAccountBtn.setPaintFlags(createAccountBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Default selection
        selectTenant();

        findRoomBtn.setOnClickListener(this);
        tenantBtn.setOnClickListener(this);
        adminBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

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

        if (id == R.id.tenantBtn) {
            selectTenant();

        } else if (id == R.id.adminBtn) {
            selectAdmin();

        } else if (id == R.id.createAccountBtn) {
            intent = new Intent(MainActivity.this, RegisterForm.class);
            intent.putExtra("accountType", accountSelected);
            startActivity(intent);

        } else if (id == R.id.findRoomBtn) {
            intent = new Intent(this, FindRoomPage.class);
            startActivity(intent);

        } else if (id == R.id.loginBtn) {
            String username = userTxt.getText().toString().trim();
            String password = passwordTxt.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tenant login
            if (accountSelected.equals("tenant")) {
                int userId = db.loginUser(username, password);

                if (userId != -1) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MainActivity.this, TenantDashboard.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Invalid tenant username or password!", Toast.LENGTH_SHORT).show();
                }

            }
            // Admin login
            else if (accountSelected.equals("admin")) {

                int adminId = db.loginUser(username, password);

                if (adminId != -1) {
                    Toast.makeText(this, "Admin Login Successful!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MainActivity.this, AdminDashboard.class);
                    intent.putExtra("userId", adminId);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Invalid admin username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void selectTenant() {
        accountSelected = "tenant";
        tenantBtn.setBackgroundResource(R.drawable.toggle_selected);
        tenantBtn.setTextColor(Color.WHITE);
        adminBtn.setBackgroundResource(R.drawable.toggle_unselected);
        adminBtn.setTextColor(Color.BLACK);
        loginBtn.setText("Login as Tenant");
        accountType.setText("Email Address");
        userTxt.setHint("Email");
    }

    private void selectAdmin() {
        accountSelected = "admin";
        adminBtn.setBackgroundResource(R.drawable.toggle_selected);
        adminBtn.setTextColor(Color.WHITE);
        tenantBtn.setBackgroundResource(R.drawable.toggle_unselected);
        tenantBtn.setTextColor(Color.BLACK);
        loginBtn.setText("Login as Admin");
        accountType.setText("Admin Account");
        userTxt.setHint("Username");
    }
}
