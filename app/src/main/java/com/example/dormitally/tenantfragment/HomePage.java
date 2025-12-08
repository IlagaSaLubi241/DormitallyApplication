package com.example.dormitally.tenantfragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dormitally.DBHelper;
import com.example.dormitally.R;
import com.example.dormitally.TenantFullDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomePage extends Fragment implements View.OnClickListener {

    View view;
    TextView totalBalanceTxt, tenantIdTxt, roomNumTxt,statusTxt;
    LinearLayout toPaymentHistory,toMessageAdmin;
    Button paynowBtn;
    int accountId;
    DBHelper dbHelper;
    private ActivityResultLauncher<Intent> homePageLauncher;
    String paymentMethod = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            accountId = getArguments().getInt("userId", -1);
        }

        view = inflater.inflate(R.layout.fragment_home_tenant, container, false);
        totalBalanceTxt = view.findViewById(R.id.totalBalance);
        tenantIdTxt = view.findViewById(R.id.tenantIdTxt);
        roomNumTxt = view.findViewById(R.id.roomNumTxt);
        statusTxt = view.findViewById(R.id.statusTxt);
        paynowBtn = view.findViewById(R.id.btnPayNow);
        dbHelper = new DBHelper(getContext());
        toPaymentHistory = view.findViewById(R.id.viewPaymentLayout);
        toMessageAdmin = view.findViewById(R.id.messageAdminLayout);
        toPaymentHistory.setOnClickListener(this);
        paynowBtn.setOnClickListener(this);
        toMessageAdmin.setOnClickListener(this);

        TenantFullDetails tenant = dbHelper.getTenantFullDetailsById(accountId);

        statusTxt.setText(tenant.getStatus());
        tenantIdTxt.setText(String.valueOf(tenant.getUserId()));
        roomNumTxt.setText(tenant.getRoomNum());
        double totalBalance = dbHelper.getTotalPendingBalance(accountId);
        totalBalanceTxt.setText(String.valueOf(totalBalance));
        return view;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.viewPaymentLayout) {
            Fragment historyPage = new HistoryPage();
            Bundle args = new Bundle();
            args.putInt("userId", accountId);
            historyPage.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, historyPage)
                    .addToBackStack(null)
                    .commit();
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_history);
        }else if (vId == R.id.messageAdminLayout) {
            Fragment messagePage = new MessagePage();
            Bundle args = new Bundle();
            args.putInt("userId", accountId);
            messagePage.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, messagePage)
                    .addToBackStack(null)
                    .commit();
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_messages);
        }else if(vId == R.id.btnPayNow){
            Fragment billsPage = new BillsPage();
            Bundle args = new Bundle();
            args.putInt("userId", accountId);
            billsPage.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, billsPage)
                    .addToBackStack(null)
                    .commit();
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_bills);
        }
    }
    /*
    @Override
    public void onClick(View v) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.paybills_tenant, null);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnProceed = view.findViewById(R.id.btnProceed);
        RelativeLayout onlinePymntLayout = view.findViewById(R.id.onlinePymntLayout);
        RelativeLayout cashPymntLayout = view.findViewById(R.id.cashPymntLayout);
        onlinePymntLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlinePymntLayout.setBackgroundColor(Color.parseColor("#CFF8D8"));
                cashPymntLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                paymentMethod = "Online Payment";
            }
        });
        cashPymntLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }*/
}