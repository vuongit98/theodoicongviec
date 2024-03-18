package com.theodoilamviec.Account;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class JobNotification implements Parcelable {
    private Job job ;
    private String idJobPermission ;
    private Long timeEnd ;
    private String idProject ;

    public JobNotification(Job job, String idJobPermission, Long timeEnd, String idProject) {
        this.job = job;
        this.idJobPermission = idJobPermission;
        this.timeEnd = timeEnd;
        this.idProject = idProject;
    }

    protected JobNotification(Parcel in) {
        job = in.readParcelable(Job.class.getClassLoader());
        idJobPermission = in.readString();
        if (in.readByte() == 0) {
            timeEnd = null;
        } else {
            timeEnd = in.readLong();
        }
        idProject = in.readString();
    }

    public static final Creator<JobNotification> CREATOR = new Creator<JobNotification>() {
        @Override
        public JobNotification createFromParcel(Parcel in) {
            return new JobNotification(in);
        }

        @Override
        public JobNotification[] newArray(int size) {
            return new JobNotification[size];
        }
    };

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public JobNotification() {
    }

    public JobNotification(Job job, Long timeEnd) {
        this.job = job;
        this.timeEnd = timeEnd;
    }
    public JobNotification(Job job, String idJobPermission, Long timeEnd) {
        this.job = job;
        this.idJobPermission = idJobPermission;
        this.timeEnd = timeEnd;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getIdJobPermission() {
        return idJobPermission;
    }

    public void setIdJobPermission(String idJobPermission) {
        this.idJobPermission = idJobPermission;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(job, flags);
        dest.writeString(idJobPermission);
        if (timeEnd == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timeEnd);
        }
        dest.writeString(idProject);
    }
}
