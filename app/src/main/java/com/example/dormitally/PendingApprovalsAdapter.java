package com.example.dormitally;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PendingApprovalsAdapter extends RecyclerView.Adapter<PendingApprovalsHolder> {
    Context context;
    List<Account> list;
    LayoutInflater inflater;

    public PendingApprovalsAdapter(Context context, List<Account> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PendingApprovalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PendingApprovalsHolder(inflater.inflate(R.layout.pending_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PendingApprovalsHolder holder, int position) {
            Account account = list.get(position);
            int accId = account.getUserId();
            holder.name.setText(account.getUsername());
            holder.occupation.setText(account.getRole());
            holder.review.setOnClickListener(v -> {
                showTenantDetails(accId);
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private void showTenantDetails(int userId) {

        DBHelper db = new DBHelper(context);
        TenantFullDetails t = db.getTenantFullDetailsById(userId);

        if (t == null) {
            Toast.makeText(context, "Error loading details", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tenant_details, null);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        TextView fullName = view.findViewById(R.id.txtFullName);
        TextView email = view.findViewById(R.id.txtEmail);
        TextView contactNumber = view.findViewById(R.id.txtContactNumber);
        TextView gender = view.findViewById(R.id.txtGender);
        TextView age = view.findViewById(R.id.txtAge);
        TextView status = view.findViewById(R.id.txtStatus);
        TextView reason = view.findViewById(R.id.txtReason);
        TextView occupation = view.findViewById(R.id.txtOccupation);
        TextView room = view.findViewById(R.id.txtRoom);
        TextView balance = view.findViewById(R.id.txtBalance);
        ImageView validID = view.findViewById(R.id.imgValidID);
        Button close = view.findViewById(R.id.btnClose);

        // SAFE SET TEXTS
        fullName.setText(t.getFullName() != null ? t.getFullName() : "N/A");
        email.setText(t.getEmail() != null ? t.getEmail() : "N/A");
        contactNumber.setText(t.getContactNum() != null ? t.getContactNum() : "N/A");
        gender.setText(t.getGender() != null ? t.getGender() : "N/A");
        age.setText(String.valueOf(t.getAge()));
        status.setText(t.getStatus() != null ? t.getStatus() : "N/A");
        reason.setText(t.getReasonForRent() != null ? t.getReasonForRent() : "N/A");
        occupation.setText(t.getCivilStatus() != null ? t.getCivilStatus() : "N/A");
        room.setText(t.getRoomNum() != null ? t.getRoomNum() : "N/A");
        balance.setText(String.valueOf(t.getTotalBalance()));

        // STATUS COLOR & BG
        String sts = t.getStatus();
        if (sts != null) {
            if (sts.equalsIgnoreCase("Active")) {
                status.setBackgroundResource(R.drawable.bg_available);
                status.setTextColor(Color.parseColor("#079732"));
            } else if (sts.equalsIgnoreCase("Pending")) {
                status.setBackgroundResource(R.drawable.bg_maintenance);
                status.setTextColor(Color.parseColor("#C8B104"));
            }
        }

        if (t.getValidId() != null) {
            try {
                Glide.with(context)
                        .load(t.getValidId())
                        .into(validID);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        builder.setView(view);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        close.setOnClickListener(v -> dialog.dismiss());
    }
}
