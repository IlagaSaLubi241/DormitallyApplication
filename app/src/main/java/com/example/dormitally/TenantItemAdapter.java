package com.example.dormitally;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TenantItemAdapter extends RecyclerView.Adapter<TenantItemHolder>{
    private Context context;
    private List<Account> list;
    private LayoutInflater inflater;
    OnItemActionListener listener;

    public TenantItemAdapter(Context context, List<Account> list, TenantItemAdapter.OnItemActionListener listener) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TenantItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TenantItemHolder(inflater.inflate(R.layout.item_tenant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TenantItemHolder holder, int position) {
        Account account = list.get(position);

        DBHelper db = new DBHelper(context);
        TenantFullDetails tenant = db.getTenantFullDetailsById(account.getUserId());
        holder.nameTxt.setText(tenant.getFullName() );
        holder.statusTxt.setText(tenant.getStatus());
        holder.emailTxt.setText(tenant.getEmail());
        holder.roomTxt.setText(tenant.getRoomNum());
        holder.balanceTxt.setText(tenant.getTotalBalance() + "");
        String status = tenant.getStatus(); // make sure you retrieve it first
        double balance = tenant.getTotalBalance();
        if (status.equalsIgnoreCase("Active")) {
            holder.statusTxt.setBackgroundResource(R.drawable.bg_available);
            holder.statusTxt.setTextColor(Color.parseColor("#079732"));
            holder.btnViewDetails.setVisibility(View.VISIBLE);
            holder.balanceTxt.setText(balance + "");
            holder.btnApprove.setVisibility(View.GONE);
        } else if (status.equalsIgnoreCase("Pending")) {
            holder.statusTxt.setTextColor(Color.parseColor("#C8B104"));
            holder.statusTxt.setBackgroundResource(R.drawable.bg_maintenance);
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnViewDetails.setVisibility(View.VISIBLE);
            holder.balanceTxt.setVisibility(View.GONE);
        }
        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ok = db.updateStatus(tenant.getUserId(), "Active");
                if(ok != 0){
                    holder.btnApprove.setVisibility(View.GONE);
                    holder.btnViewDetails.setVisibility(View.VISIBLE);
                    holder.statusTxt.setText("Active");
                    db.setRoomStatus(tenant.getRoomNum(),"Occupied");
                    Toast.makeText(context, "Tenant approved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Error approving tenant", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDelete(tenant, holder.getAdapterPosition());
                    db.setRoomStatus(tenant.getRoomNum(),"Available");
                }
            }
        });
        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTenantDetails(tenant.getUserId());
            }
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
    public interface OnItemActionListener {
         // Open AddRoomPage for editing
        void onDelete(TenantFullDetails room, int position); // Confirm deletion
    }


}
