    package com.example.dormitally.tenantfragment;

    import android.app.AlertDialog;
    import android.graphics.Color;
    import android.os.Build;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.RelativeLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.annotation.RequiresApi;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.dormitally.Balance;
    import com.example.dormitally.DBHelper;
    import com.example.dormitally.R;
    import com.example.dormitally.TenantBillAdapter;
    import com.google.android.material.bottomsheet.BottomSheetDialog;

    import java.util.ArrayList;
    import java.util.List;

    public class BillsPage extends Fragment implements View.OnClickListener {

        View view;
        List<Balance> balances = new ArrayList<>();

        TextView currentBillTotal, currentBillDue, monthlyRentalTxt, electricBalanceTxt,
                waterBalanceTxt, wifiBalanceTxt, currentBillTotal2, txtTotalPaid, txtPending;

        int accountId;
        DBHelper dbHelper;
        Button billPay;
        RecyclerView recyclerView;
        TenantBillAdapter adapter;
        Balance latestBalance;
        double totalPendingBalance, totalPaidBalance;
        String paymentMethod = "";

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_bills_tenant, container, false);

            // Get userId from Fragment arguments
            if (getArguments() != null) {
                accountId = getArguments().getInt("userId", -1);
            }

            dbHelper = new DBHelper(getContext());

            // Retrieve balance data
            latestBalance = dbHelper.getLatestBalance(accountId);
            totalPendingBalance = dbHelper.getAccountPendingBalance(accountId);
            totalPaidBalance = dbHelper.getAccountPiadBalance(accountId);

            // Initialize UI
            initViews();

            // Set balance values
            setBalanceValues();

            // Setup list
            setupRecyclerView();

            // Pay button
            billPay.setOnClickListener(this);

            return view;
        }

        private void initViews() {
            currentBillTotal = view.findViewById(R.id.currentBillTotal);
            currentBillTotal2 = view.findViewById(R.id.currentBillTotal2);
            currentBillDue = view.findViewById(R.id.currentBillDue);

            monthlyRentalTxt = view.findViewById(R.id.monthlyRentalTxt);
            electricBalanceTxt = view.findViewById(R.id.electricBalanceTxt);
            waterBalanceTxt = view.findViewById(R.id.waterBalanceTxt);
            wifiBalanceTxt = view.findViewById(R.id.wifiBalanceTxt);

            txtTotalPaid = view.findViewById(R.id.txtTotalPaid);
            txtPending = view.findViewById(R.id.txtPending);
            billPay = view.findViewById(R.id.btnPayBill);

            recyclerView = view.findViewById(R.id.recyclerBills);
        }

        private void setBalanceValues() {
            txtTotalPaid.setText(String.format("₱%.2f", totalPaidBalance));
            txtPending.setText(String.format("₱%.2f", totalPendingBalance));

            if (latestBalance == null || latestBalance.getTotalBalance() <= 0 ||
                    "Paid".equalsIgnoreCase(latestBalance.getBalanceStatus())) {

                // No current unpaid bill
                currentBillTotal.setText("₱0.00");
                currentBillTotal2.setText("₱0.00");
                currentBillDue.setText("No Current Bill");

                monthlyRentalTxt.setText("₱0.00");
                electricBalanceTxt.setText("₱0.00");
                waterBalanceTxt.setText("₱0.00");
                wifiBalanceTxt.setText("₱0.00");

                // Hide Pay Button
                billPay.setVisibility(View.GONE);

            } else {

                // There is an active unpaid bill
                billPay.setVisibility(View.VISIBLE);

                currentBillTotal.setText(String.format("₱%.2f", latestBalance.getTotalBalance()));
                currentBillTotal2.setText(String.format("₱%.2f", latestBalance.getTotalBalance()));
                currentBillDue.setText(latestBalance.getDueDate());

                electricBalanceTxt.setText(String.format("₱%.2f", latestBalance.getElectricBalance()));
                waterBalanceTxt.setText(String.format("₱%.2f", latestBalance.getWaterBalance()));
                wifiBalanceTxt.setText(String.format("₱%.2f", latestBalance.getWifiBalance()));
                monthlyRentalTxt.setText(String.format("₱%.2f", latestBalance.getMonthlyRentBalance()));
            }
        }


        private void setupRecyclerView() {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            balances = dbHelper.getAllBalancesByUserId(accountId);

            adapter = new TenantBillAdapter(getContext(), balances, new TenantBillAdapter.OnItemActionListener() {
                @Override
                public void onPayClick(Balance balance) {
                    if (balance.getTotalBalance() > 0 &&
                            !"Paid".equalsIgnoreCase(balance.getBalanceStatus())) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            openPaymentBottomSheet(balance.getTotalBalance(), balance.getBalanceId());
                        }
                    }
                }

                @Override
                public void onItemClick(Balance balance) {
                    // Handle bill details if needed
                }
            });

            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {

            if (latestBalance == null || latestBalance.getTotalBalance() <= 0 ||
                    "Paid".equalsIgnoreCase(latestBalance.getBalanceStatus())) {
                return; // no bill to pay
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                openPaymentBottomSheet(latestBalance.getTotalBalance(), latestBalance.getBalanceId());
            }
        }



        @RequiresApi(api = Build.VERSION_CODES.O)
        private void openPaymentBottomSheet(double totalAmount, int balanceId) {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View sheetView = getLayoutInflater().inflate(R.layout.paybills_tenant, null);

            Button btnCancel = sheetView.findViewById(R.id.btnCancel);
            Button btnProceed = sheetView.findViewById(R.id.btnProceed);
            TextView txtTotal = sheetView.findViewById(R.id.txtAmountValue);
            RelativeLayout onlinePymntLayout = sheetView.findViewById(R.id.onlinePymntLayout);
            RelativeLayout cashPymntLayout = sheetView.findViewById(R.id.cashPymntLayout);

            txtTotal.setText(String.format("₱%.2f", totalAmount));

            paymentMethod = "";

            // Select Online Payment
            onlinePymntLayout.setOnClickListener(v -> {
                paymentMethod = "Online Payment";
                onlinePymntLayout.setBackgroundColor(Color.parseColor("#CFF8D8"));
                cashPymntLayout.setBackgroundColor(Color.WHITE);
            });

            // Select Cash Payment
            cashPymntLayout.setOnClickListener(v -> {
                paymentMethod = "Cash Payment";
                cashPymntLayout.setBackgroundColor(Color.parseColor("#CFF8D8"));
                onlinePymntLayout.setBackgroundColor(Color.WHITE);
            });

            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());


            // ---------------------------------
            // PROCEED BUTTON LOGIC
            // ---------------------------------
            btnProceed.setOnClickListener(v -> {

                // Must choose payment method
                if (paymentMethod.equals("")) {
                    Toast.makeText(getContext(), "Please choose a payment method", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create paidDate
                String paidDate = java.time.LocalDate.now().toString();

                // For now reference number is optional
                String referenceNum = paymentMethod.equals("Cash Payment") ? "" : "n/a";

                // INSERT INTO paymentInfo
                boolean added = dbHelper.addPaymentInfo(
                        balanceId,
                        accountId,
                        totalAmount,
                        paymentMethod,
                        referenceNum,
                        paidDate
                );

                if (!added) {
                    Toast.makeText(getContext(), "Error saving payment info!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // After saving payment info -> update balance
                boolean updated = dbHelper.updateBalanceToPaid(balanceId);

                if (updated) {

                    bottomSheetDialog.dismiss();
                    showPaymentSuccessDialog();

                    // Refresh UI values
                    latestBalance = dbHelper.getLatestBalance(accountId);
                    totalPendingBalance = dbHelper.getAccountPendingBalance(accountId);
                    totalPaidBalance = dbHelper.getAccountPiadBalance(accountId);

                    setBalanceValues();

                    // Refresh list
                    balances.clear();
                    balances.addAll(dbHelper.getAllBalancesByUserId(accountId));
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "Failed to update balance!", Toast.LENGTH_SHORT).show();
                }

            });

            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        }

        // --------------------------
        // PAYMENT SUCCESS POPUP
        // --------------------------
        private void showPaymentSuccessDialog() {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View popupView = inflater.inflate(R.layout.payment_complete_popup, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(popupView);

            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);

            Button btnOkay = popupView.findViewById(R.id.btnOkay);
            btnOkay.setOnClickListener(v -> dialog.dismiss() );

            dialog.show();
        }
    }
