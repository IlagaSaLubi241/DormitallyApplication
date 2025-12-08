package com.example.dormitally;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateBill extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner tenant, month;
    EditText rentEt, electricEt, waterEt, wifiEt, maintenanceEt;
    Double rent, electric, water, wifi, maintenance;
    String tenantStr, monthStr, yearStr, dueDateStr, dateStr;
    TextView totalAmount;
    DatePicker datePicker;
    Calendar today;
    NumberPicker yearPicker;
    Button cancelBtn, createBillBtn;
    Double total = 0.0;
    DBHelper db;
    ArrayList<String> tenants;

    int userId;
    private int balanceId = -1;
    private boolean isEditing = false;

    ArrayAdapter<String> tenantAdapter;
    ArrayAdapter<CharSequence> monthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_bill);

        // DB
        db = new DBHelper(this);
        tenants = db.getAllActiveTenantNames();

        // Views
        cancelBtn = findViewById(R.id.cancelBtn);
        createBillBtn = findViewById(R.id.createBillBtn);
        datePicker = findViewById(R.id.datePicker);
        rentEt = findViewById(R.id.rentEt);
        electricEt = findViewById(R.id.electricEt);
        waterEt = findViewById(R.id.waterEt);
        wifiEt = findViewById(R.id.wifiEt);
        maintenanceEt = findViewById(R.id.maintenanceEt);
        tenant = findViewById(R.id.spinnerTenant);
        month = findViewById(R.id.spinnerMonth);
        totalAmount = findViewById(R.id.totalAmountTxt);
        yearPicker = findViewById(R.id.yearPicker);

        // -----------------------------
        // FIX 1: Initialize SPINNER adapters BEFORE reading bundle
        // -----------------------------
        tenantAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tenants);
        tenantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tenant.setAdapter(tenantAdapter);

        monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.Month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(monthAdapter);

        // -----------------------------
        // FIX 2: Handle editing bundle safely
        // -----------------------------
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            isEditing = true;

            balanceId = bundle.getInt("balanceId");
            userId = bundle.getInt("userId");
            rent = bundle.getDouble("rent");
            electric = bundle.getDouble("electric");
            water = bundle.getDouble("water");
            wifi = bundle.getDouble("wifi");
            maintenance = bundle.getDouble("maintenance");
            total = bundle.getDouble("total");
            tenantStr = bundle.getString("tenant");
            dueDateStr = bundle.getString("dueDate");

            // Restore DatePicker if valid date
            if (dueDateStr != null && dueDateStr.contains("/")) {
                String[] d = dueDateStr.split("/");
                int day = Integer.parseInt(d[0]);
                int month = Integer.parseInt(d[1]) - 1;
                int year = Integer.parseInt(d[2]);
                datePicker.updateDate(year, month, day);
            }


            // FIX 3: Safely split "January 2025"
            String dateString = bundle.getString("date", "January 2025");
            String[] parts = dateString.split(" ");

            if (parts.length == 2) {
                monthStr = parts[0];
                yearStr = parts[1];
            } else {
                monthStr = "January";
                yearStr = "2025";
            }

            // Fill inputs
            rentEt.setText(String.valueOf(rent));
            electricEt.setText(String.valueOf(electric));
            waterEt.setText(String.valueOf(water));
            wifiEt.setText(String.valueOf(wifi));
            maintenanceEt.setText(String.valueOf(maintenance));

            // FIX 4: Safe spinner selection
            int tenantPos = tenantAdapter.getPosition(tenantStr);
            if (tenantPos >= 0) tenant.setSelection(tenantPos);

            int monthPos = monthAdapter.getPosition(monthStr);
            if (monthPos >= 0) month.setSelection(monthPos);

            createBillBtn.setText("Update Bill");
        }

        // Listeners
        tenant.setOnItemSelectedListener(this);
        month.setOnItemSelectedListener(this);
        createBillBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        // Watching for changes
        TextWatcher watcher = new SimpleWatcher();
        electricEt.addTextChangedListener(watcher);
        waterEt.addTextChangedListener(watcher);
        wifiEt.addTextChangedListener(watcher);
        maintenanceEt.addTextChangedListener(watcher);

        calculateTotal();

        // Date Picker
        today = Calendar.getInstance();
        datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                (view, year, m, day) -> {
                    dueDateStr = day + "/" + (m + 1) + "/" + year;
                }
        );

        dueDateStr = today.get(Calendar.DAY_OF_MONTH) + "/" +
                (today.get(Calendar.MONTH) + 1) + "/" +
                today.get(Calendar.YEAR);



        // Year Picker
        yearPicker.setMinValue(2000);
        yearPicker.setMaxValue(2100);
        yearPicker.setValue(Integer.parseInt(yearStr == null ? "2025" : yearStr));
        yearStr = String.valueOf(yearPicker.getValue());

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Safe parsing for EditTexts
    private double parseDoubleSafe(EditText et) {
        try {
            String s = et.getText().toString().trim();
            return s.isEmpty() ? 0 : Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelBtn) {
            finish();
            return;
        }

        if (v.getId() == R.id.createBillBtn) {

            Intent intent = new Intent();

            int uid = db.getUserIdByFullName(tenantStr);
            if (uid == -1) {
                Toast.makeText(this, "Invalid tenant!", Toast.LENGTH_SHORT).show();
                return;
            }

            rent = db.getMonthlyRentBalance(uid);
            dateStr = monthStr + " " + yearStr;
            calculateTotal();

            if (isEditing) {
                intent.putExtra("balanceId", balanceId);
            }

            intent.putExtra("userId", uid);
            intent.putExtra("wifi", wifi);
            intent.putExtra("rent", rent);
            intent.putExtra("electric", electric);
            intent.putExtra("water", water);
            intent.putExtra("maintenance", maintenance);
            intent.putExtra("total", total);
            intent.putExtra("status", "Pending");
            intent.putExtra("verification", "Unverified");
            intent.putExtra("dueDate", dueDateStr);
            intent.putExtra("date", dateStr);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.spinnerTenant) {
            tenantStr = tenant.getSelectedItem().toString();
            userId = db.getUserIdByFullName(tenantStr);
        }

        if (parent.getId() == R.id.spinnerMonth) {
            monthStr = month.getSelectedItem().toString();

            if (!monthStr.equals("Month") && userId != -1) {
                rent = db.getMonthlyRentBalance(userId);
                rentEt.setText(String.valueOf(rent));
                calculateTotal();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private class SimpleWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { calculateTotal(); }
    }

    private void calculateTotal() {
        rent = parseDoubleSafe(rentEt);
        electric = parseDoubleSafe(electricEt);
        water = parseDoubleSafe(waterEt);
        wifi = parseDoubleSafe(wifiEt);
        maintenance = parseDoubleSafe(maintenanceEt);

        total = rent + electric + water + wifi + maintenance;
        totalAmount.setText("Total Amount: ₱" + total);
    }
}
