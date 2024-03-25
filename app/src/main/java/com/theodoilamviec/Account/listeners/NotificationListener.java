package com.theodoilamviec.Account.listeners;

import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.Account.TempNotificationObject;

public interface NotificationListener {
    void onNotificationClicked(TempNotificationObject notification, int position);
}
