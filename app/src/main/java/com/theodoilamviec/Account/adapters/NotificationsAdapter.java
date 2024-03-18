package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.Account.listeners.NotificationListener;
import com.theodoilamviec.theodoilamviec.databinding.ItemNotificationBinding;


import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private final List<String> notificationsJobList = new ArrayList<>();
    private final NotificationListener notificationListener;

    public NotificationsAdapter( NotificationListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<String> jobNotifications) {
        notificationsJobList.clear();
        notificationsJobList.addAll(jobNotifications);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.set_notification(notificationsJobList.get(position), position, notificationListener);

    }

    @Override
    public int getItemCount() {
        return notificationsJobList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        ItemNotificationBinding binding ;
        NotificationViewHolder(@NonNull ItemNotificationBinding notification) {
            super(notification.getRoot());
            binding = notification;
        }

        void set_notification(String data, int position, NotificationListener notificationListener) {
            binding.notificationTitle.setText(data);
            binding.itemNotificationLayout.setOnClickListener(v -> notificationListener
                    .onNotificationClicked(data, position));
        }

    }

}
