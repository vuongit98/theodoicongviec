package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.theodoilamviec.Account.Message;
import com.theodoilamviec.theodoilamviec.databinding.ItemLeftImageBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemLeftMessageBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemRightImageBinding;
import com.theodoilamviec.theodoilamviec.databinding.ItemRightMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PDF_LEFT = 1;
    public static final int IMAGE_LEFT = 2;
    public static final int OTHER_LEFT = 3;
    public static final int PDF_RIGHT = 4;
    public static final int IMAGE_RIGHT = 5;
    public static final int OTHER_RIGHT = 6;

    private final List<Message> messagesList = new ArrayList<>();

    private String uid = "";

    private final Context mContext ;
    public MessageListAdapter(String uid, Context context) {
        System.out.println("uid = " + uid);
        this.uid = uid;
        this.mContext = context;
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
        if (message.getUidSentMessage().equals(uid)) {
            if (message.getTypeMessage() == PDF_LEFT) {
                return PDF_RIGHT   ;
            }else if(message.getTypeMessage() == IMAGE_LEFT) {
                return IMAGE_RIGHT ;
            }else if (OTHER_LEFT == message.getTypeMessage()) {
                return OTHER_RIGHT  ;
            }else {
                return message.getTypeMessage();
            }
        }else {
            if (message.getTypeMessage() == PDF_RIGHT) {
                return  PDF_LEFT;
            }else if(message.getTypeMessage() == IMAGE_RIGHT) {
                return  IMAGE_LEFT;
            }else if (OTHER_RIGHT == message.getTypeMessage()) {
                return OTHER_LEFT ;
            }else {
                return message.getTypeMessage();
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
//            case PDF_LEFT : {
//                return new LeftMessageViewHolder(ItemLeftMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
//            }
            case IMAGE_LEFT: {
                return new LeftImageViewHolder(ItemLeftImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case OTHER_LEFT: {
                return new LeftMessageViewHolder(ItemLeftMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
//            case PDF_RIGHT : {
//                return new RightMessageViewHolder(ItemRightMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
//            }
            case IMAGE_RIGHT: {
                return new RightImageViewHolder(ItemRightImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case OTHER_RIGHT: {
                return new RightMessageViewHolder(ItemRightMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            default:
                return new RightMessageViewHolder(ItemRightMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messagesList.get(position);
        if (holder instanceof LeftMessageViewHolder) {
            ((LeftMessageViewHolder) holder).setLeftMessage(message);
        } else if (holder instanceof RightMessageViewHolder) {
            ((RightMessageViewHolder) holder).setRightMessage(message);
        } else if (holder instanceof LeftImageViewHolder) {
            ((LeftImageViewHolder) holder).setLeftImage(message,mContext);
        } else if (holder instanceof RightImageViewHolder) {
            ((RightImageViewHolder) holder).setRightImage(message,mContext);
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
            binding = itemView;
        }

        public void setLeftMessage(Message message) {
            binding.tvMessgage.setText(message.getMessage());
        }
    }

    static class RightMessageViewHolder extends RecyclerView.ViewHolder {
        ItemRightMessageBinding binding;

        public RightMessageViewHolder(@NonNull ItemRightMessageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setRightMessage(Message message) {
            binding.tvMessgage.setText(message.getMessage());
        }
    }

    static class LeftImageViewHolder extends RecyclerView.ViewHolder {

        ItemLeftImageBinding binding;

        public LeftImageViewHolder(@NonNull ItemLeftImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setLeftImage(Message message, Context context) {
            Glide.with(context).load(message.getMessage()).into(binding.ivContent);
        }
    }

    static class RightImageViewHolder extends RecyclerView.ViewHolder {

        ItemRightImageBinding binding;

        public RightImageViewHolder(@NonNull ItemRightImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setRightImage(Message message, Context context) {
            Glide.with(context).load(message.getMessage()).into(binding.ivContent);
        }
    }
}
