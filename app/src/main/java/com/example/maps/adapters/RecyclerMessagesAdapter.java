package com.example.maps.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maps.R;
import com.example.maps.entity.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerMessagesAdapter extends RecyclerView.Adapter<RecyclerMessagesAdapter.MessagesViewHolder> {
    public static final int OWNER_MESSAGE = 2244;
    public static final int FOREIGN_MESSAGE = 5567;

    List<Message> messages;
    int userId;

    public void setUserId(int userId) {
        this.userId = userId;
        notifyDataSetChanged();
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView messageTitle;
        TextView messgeText;
        Message message;


        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTitle = itemView.findViewById(R.id.messageTitle);
            messgeText = itemView.findViewById(R.id.messageText);
        }

        public void bind(Message msg) {
            message = msg;
            SimpleDateFormat format = new SimpleDateFormat("d MMMM y г.", Locale.getDefault());
            messageTitle.setText(msg.name + " " +
                    format.format(new Date(msg.messageDate)));
            messgeText.setText(msg.message);
        }
    }

    public void setItems(Message[] msgs) {


        messages = new ArrayList<>();
        for (Message msg : msgs) {
            messages.add(msg);
        }
        notifyDataSetChanged();
    }

    public void setItems(List<Message> msgs) {
        messages = msgs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case FOREIGN_MESSAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_layout_other, parent, false);
                return new RecyclerMessagesAdapter.MessagesViewHolder(view);
            case OWNER_MESSAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_layout_your, parent, false);
                return new RecyclerMessagesAdapter.MessagesViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).clientId == userId) {
            return OWNER_MESSAGE;
        } else {
            return FOREIGN_MESSAGE;
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
