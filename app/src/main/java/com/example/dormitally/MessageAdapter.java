package com.example.dormitally;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {
    Context context;
    List<IssueMessage> list;
    LayoutInflater inflater;

    public MessageAdapter(Context context, List<IssueMessage> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageHolder(inflater.inflate(R.layout.message_admin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
            IssueMessage message = list.get(position);

            holder.titleTxt.setText(message.getTitle());
            holder.dateTxt.setText(String.valueOf(message.getTimestamp()));
            holder.senderTxt.setText(String.valueOf(message.getSenderId()));
            holder.messageTxt.setText(message.getMessageText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void updateList(List<IssueMessage> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

}
