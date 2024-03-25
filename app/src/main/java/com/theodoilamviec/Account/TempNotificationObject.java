package com.theodoilamviec.Account;

public class TempNotificationObject {
    public String contentJob ;
    public String nameJob ;

    public TempNotificationObject() {
    }

    public TempNotificationObject(String contentJob, String nameJob) {
        this.contentJob = contentJob;
        this.nameJob = nameJob;
    }

    @Override
    public String toString() {
        return "TempNotificationObject{" +
                "contentJob='" + contentJob + '\'' +
                ", nameJob='" + nameJob + '\'' +
                '}';
    }
}
