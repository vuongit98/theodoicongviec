package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Account.Message;
import com.theodoilamviec.theodoilamviec.databinding.ItemLeftMessageBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemRightMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Message> messagesList = new ArrayList<>();

    private String uid = "";
    public MessageListAdapter(String uid) {
        System.out.println("uid = "+ uid);
        this.uid = uid ;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Message> data) {
        messagesList.clear();
        messagesList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messagesList.get(position);
        if (message.getUidSentMessage().equals(uid)){
            return  1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new LeftMessageViewHolder(ItemLeftMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        return new RightMessageViewHolder(ItemRightMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messagesList.get(position);
        if (holder instanceof LeftMessageViewHolder) {
            ((LeftMessageViewHolder) holder).setLeftMessage(message);
        }else if (holder instanceof RightMessageViewHolder) {
            ((RightMessageViewHolder) holder).setRightMessage(message);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    static class LeftMessageViewHolder extends RecyclerView.ViewHolder {

        ItemLeftMessageBinding binding;
        public LeftMessageViewHolder(@NonNull ItemLeftMessageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView ;
        }

        public void setLeftMessage(Message message){
            binding.tvMessgage.setText(message.getMessage());
        }
    }

    static class RightMessageViewHolder extends RecyclerView.ViewHolder {

        ItemRightMessageBinding binding ;
        public RightMessageViewHolder(@NonNull ItemRightMessageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView ;
        }

        public void setRightMessage(Message message) {
            binding.tvMessgage.setText(message.getMessage());
        }
    }
}
