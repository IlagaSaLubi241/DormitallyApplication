package com.example.dormitally;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TenantItemAdapter extends RecyclerView.Adapter<TenantItemHolder>{
    private Context context;
    private List<Room> list;
    private LayoutInflater inflater;
    private RoomItemAdapter.OnItemActionListener listener;

    public TenantItemAdapter(Context context, List<Room> list, RoomItemAdapter.OnItemActionListener listener) {
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
