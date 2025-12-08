package com.example.dormitally.adminfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitally.AdminBillAdapter;
import com.example.dormitally.Balance;
import com.example.dormitally.CreateBill;
import com.example.dormitally.DBHelper;
import com.example.dormitally.R;
import com.example.dormitally.Room;
import com.example.dormitally.TenantBillDetails;

import java.util.ArrayList;
import java.util.List;

public class BillsAdmin extends Fragment implements View.OnClickListener, AdminBillAdapter.OnItemActionListener {

    View view;
    Button addBillBtn;
    RecyclerView recyclerView;
    AdminBillAdapter adapter;
    List<TenantBillDetails> billList = new ArrayList<>();
    DBHelper dbHelper;
    int userId,balanceId;
    double wifi ;
    double rent  ;
    double electric ;
    double water ;
    double total ;
    double maintenance ;
    String status;
    String dueDate ;
    String verification ;
    String date ;
    int editingPosition = -1;
    boolean isEditing = false;
    private ActivityResultLauncher<Intent> billLauncher;
    TextView totalCollected,pending;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bills_admin, container, false);

        recyclerView = view.findViewById(R.id.recyclerBills);
        addBillBtn = view.findViewById(R.id.btnAddBill);
        totalCollected = view.findViewById(R.id.totalCollectedTxt);
        pending = view.findViewById(R.id.pendingTxt);

        addBillBtn.setOnClickListener(this);

        dbHelper = new DBHelper(getContext());
        billList = dbHelper.getAllTenantBills();
        refreshSummary();

        billLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null)
                        return;

                    Bundle b = result.getData().getExtras();
                    if (b == null) return;

                    balanceId = b.getInt("balanceId");
                    userId = b.getInt("userId");
                    wifi = b.getDouble("wifi");
                    rent  = b.getDouble("rent");
                    electric = b.getDouble("electric");
                    water = b.getDouble("water");
                    total = b.getDouble("total");
                    maintenance = b.getDouble("maintenance");
                    status = b.getString("status");
                    dueDate = b.getString("dueDate");
                    verification = b.getString("verification");
                    date = b.getString("date");

                    if (isEditing) {
                        // UPDATE LOGIC
                        Balance updated = new Balance(userId, balanceId, wifi, rent, electric, water,
                                total, maintenance, status, dueDate, verification, date);

                        long ok = dbHelper.updateBalance(updated);

                        if (ok > 0) {
                            TenantBillDetails updatedBill =  billList.get(editingPosition);;
                            updatedBill.setWifiBalance(wifi);
                            updatedBill.setMonthlyRentBalance(rent);
                            updatedBill.setElectricBalance(electric);
                            updatedBill.setWaterBalance(water);
                            updatedBill.setTotalBalance(total);
                            updatedBill.setMaintenanceFee(maintenance);
                            updatedBill.setBalanceStatus(status);
                            updatedBill.setDueDate(dueDate);
                            updatedBill.setVerification(verification);
                            updatedBill.setDate(date);

                            adapter.notifyDataSetChanged();
                            refreshSummary();
                            Toast.makeText(getActivity(), "Bill updated!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // ADD NEW BILL LOGIC
                        // ADD NEW BILL LOGIC
                        Balance balance = new Balance(userId, wifi, rent, electric, water, total,
                                maintenance, status, dueDate, verification, date);

                        long ok = dbHelper.addBalance(balance);

                        if (ok != -1) {
                            billList.clear();
                            billList.addAll(dbHelper.getAllTenantBills());
                            refreshSummary();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Bill created!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "Failed to create bill!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    // RESET FLAGS
                    isEditing = false;
                    editingPosition = -1;
                }
        );

        adapter = new AdminBillAdapter(getContext(), billList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddBill) {
            Intent intent = new Intent(getActivity(), CreateBill.class);
            isEditing = false;
            billLauncher.launch(intent);
        }
    }


    @Override
    public void onEdit(TenantBillDetails bill, int position) {
        Intent intent = new Intent(getActivity(), CreateBill.class);

        // Send actual values of the bill you clicked
        intent.putExtra("balanceId", bill.getBalanceId());
        intent.putExtra("userId", bill.getUserId());
        intent.putExtra("rent", bill.getMonthlyRentBalance());
        intent.putExtra("wifi", bill.getWifiBalance());
        intent.putExtra("electric", bill.getElectricBalance());
        intent.putExtra("water", bill.getWaterBalance());
        intent.putExtra("maintenance", bill.getMaintenanceFee());
        intent.putExtra("total", bill.getTotalBalance());

        intent.putExtra("tenant", bill.getFullName());  // very important
        intent.putExtra("date", bill.getDate());        // Example: "January 2025"
        intent.putExtra("dueDate", bill.getDueDate());
        intent.putExtra("status", bill.getBalanceStatus());
        intent.putExtra("verification", bill.getVerification());

        isEditing = true;
        editingPosition = position;

        billLauncher.launch(intent);
    }

    @Override
    public void onDelete(TenantBillDetails bill, int position) {

        if (position < 0 || position >= billList.size()) {
            Toast.makeText(getActivity(), "Invalid item!", Toast.LENGTH_SHORT).show();
            return;
        }

        int deleted = dbHelper.deleteBalance(bill.getBalanceId());

        if (deleted > 0) {

            billList.remove(position);

            adapter.notifyItemRemoved(position);
            refreshSummary();
            adapter.notifyItemRangeChanged(position, billList.size());

            Toast.makeText(getActivity(), "Balance deleted", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "Delete failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onView(int balanceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.view_billdetails_popup, null);
        builder.setView(view);

        TenantBillDetails tb = dbHelper.getTenantBillDetailsByBalanceId(balanceId);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // ----- FIND VIEWS -----
        TextView txtBillPeriod = view.findViewById(R.id.txtBillPeriod);
        TextView txtBillDueDate = view.findViewById(R.id.txtBillDueDate);
        TextView txtBillStatus = view.findViewById(R.id.txtBillStatus);
        TextView txtBillVerification = view.findViewById(R.id.txtBillVerification);
        TextView txtModeOfPayment = view.findViewById(R.id.txtModeOfPayment);

        TextView txtRent = view.findViewById(R.id.txtRent);
        TextView txtElectric = view.findViewById(R.id.txtElectric);
        TextView txtWater = view.findViewById(R.id.txtWater);
        TextView txtWifi = view.findViewById(R.id.txtWifi);
        TextView txtMaintenance = view.findViewById(R.id.txtMaintenance);
        TextView txtTotalAmount = view.findViewById(R.id.txtTotalAmount);

        Button btnClose = view.findViewById(R.id.btnClose);

        // ----- SET DATA -----
        txtBillPeriod.setText(tb.getDate());
        txtBillDueDate.setText(tb.getDueDate());
        txtBillStatus.setText(tb.getBalanceStatus());
        txtBillVerification.setText(tb.getVerification());

        txtRent.setText("₱" + tb.getMonthlyRentBalance());
        txtElectric.setText("₱" + tb.getElectricBalance());
        txtWater.setText("₱" + tb.getWaterBalance());
        txtWifi.setText("₱" + tb.getWifiBalance());
        txtMaintenance.setText("₱" + tb.getMaintenanceFee());
        txtTotalAmount.setText("Total: ₱" + tb.getTotalBalance());

        // ----- SHOW PAYMENT METHOD ONLY IF PAID -----
        if (tb.getBalanceStatus().equalsIgnoreCase("Paid")) {
            txtModeOfPayment.setVisibility(View.VISIBLE);
            txtModeOfPayment.setText(tb.getPaymentMethod());
        } else {
            txtModeOfPayment.setVisibility(View.GONE);
        }

        // CLOSE
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    @Override
    public void onVerify(TenantBillDetails bill, int position) {

        boolean success = dbHelper.verifyPaymentByBalanceId(bill.getBalanceId());

        if (success) {
            // Update UI
            bill.setVerification("Verified");
            bill.setBalanceStatus("Paid");

            adapter.notifyItemChanged(position);
            refreshSummary();

            Toast.makeText(getContext(), "Payment Verified Successfully!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Verification Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshSummary() {
        double pendingAmount = dbHelper.getTotalPendingAmount();
        double collectedAmount = dbHelper.getTotalCollectedAmount();

        pending.setText(String.format("₱%.2f", pendingAmount));
        totalCollected.setText(String.format("₱%.2f", collectedAmount));
    }


}
