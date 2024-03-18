package com.theodoilamviec.Account.listeners;

import com.theodoilamviec.Account.JobNotificationLocal;

public interface NotificationListener {
    void onNotificationClicked(String notification, int position);
}
