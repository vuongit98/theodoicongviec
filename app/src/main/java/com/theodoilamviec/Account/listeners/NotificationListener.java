package com.theodoilamviec.Account.listeners;

import com.theodoilamviec.theodoilamviec.models.Notification;

public interface NotificationListener {
    void onNotificationClicked(Notification notification, int position);
}
