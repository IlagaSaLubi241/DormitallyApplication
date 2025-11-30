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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button tenantBtn, adminBtn,loginBtn;
    TextView createAccountBtn,accountType;
    EditText userEditText;
    LinearLayout findRoomBtn;
    String accountSelected; // default selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        accountType = findViewById(R.id.accountType);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        tenantBtn = findViewById(R.id.tenantBtn);
        adminBtn = findViewById(R.id.adminBtn);
        findRoomBtn = findViewById(R.id.findRoomBtn);
        loginBtn = findViewById(R.id.loginBtn);
        createAccountBtn.setPaintFlags(createAccountBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        userEditText = findViewById(R.id.emailEditTxt);
        findRoomBtn.setOnClickListener(this);
        tenantBtn.setOnClickListener(this);
        adminBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        // Set default colors
        selectTenant();

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
            intent.putExtra("accountType", accountSelected); // pass selection
            startActivity(intent);
        }else if (id == R.id.findRoomBtn){
            intent = new Intent(this,FindRoomPage.class);
            startActivity(intent);
        }else if(id == R.id.loginBtn){
            if(accountSelected == "tenant"){
                intent = new Intent(MainActivity.this,TenantDashboard.class);
                startActivity(intent);
            }else{
                intent = new Intent(MainActivity.this,AdminDashboard.class);
                startActivity(intent);
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
        userEditText.setHint("Email");
    }

    private void selectAdmin() {
        accountSelected = "admin";
        adminBtn.setBackgroundResource(R.drawable.toggle_selected);
        adminBtn.setTextColor(Color.WHITE);
        tenantBtn.setBackgroundResource(R.drawable.toggle_unselected);
        tenantBtn.setTextColor(Color.BLACK);
        loginBtn.setText("Login as Admin");
        accountType.setText("Admin Account");
        userEditText.setHint("Username");
    }
}
