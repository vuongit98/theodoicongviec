package com.theodoilamviec.Account;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "Jobs")
public class Job implements Parcelable {
    private String idJob = String.valueOf(System.currentTimeMillis());
    private String nameJob ;
    private Long timeStartDate ;
    private Long timeEndDate ;
    private int highPriority;
    private String idProject ;
    private boolean isDeleted = false;

    private int statusJob = 0 ; // new:0 , responsing: 1 ,finished: 2, unfinished: 3
    public Job() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Job(String idJob, String nameJob, Long timeStartDate, Long timeEndDate, int highPriority, String idProject, int statusJob, boolean isDeleted) {
        this.idJob = idJob;
        this.nameJob = nameJob;
        this.timeStartDate = timeStartDate;
        this.timeEndDate = timeEndDate;
        this.highPriority = highPriority;
        this.idProject = idProject;
        this.statusJob = statusJob;
        this.isDeleted = isDeleted;
    }

    protected Job(Parcel in) {
        idJob = in.readString();
        nameJob = in.readString();
        if (in.readByte() == 0) {
            timeStartDate = null;
        } else {
            timeStartDate = in.readLong();
        }
        if (in.readByte() == 0) {
            timeEndDate = null;
        } else {
            timeEndDate = in.readLong();
        }
        highPriority = in.readInt();
        idProject = in.readString();
        statusJob = in.readInt();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isDeleted = in.readBoolean();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idJob);
        dest.writeString(nameJob);
        if (timeStartDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timeStartDate);
        }
        if (timeEndDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timeEndDate);
        }
        dest.writeInt(highPriority);
        dest.writeString(idProject);
        dest.writeInt(statusJob);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(isDeleted);
        }else {
            dest.writeInt(0);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getNameJob() {
        return nameJob;
    }

    public void setNameJob(String nameJob) {
        this.nameJob = nameJob;
    }

    public Long getTimeStartDate() {
        return timeStartDate;
    }

    public void setTimeStartDate(Long timeStartDate) {
        this.timeStartDate = timeStartDate;
    }

    public Long getTimeEndDate() {
        return timeEndDate;
    }

    public void setTimeEndDate(Long timeEndDate) {
        this.timeEndDate = timeEndDate;
    }

    public int getHighPriority() {
        return highPriority;
    }

    public int getStatusJob() {
        return statusJob;
    }

    public void setStatusJob(int statusJob) {
        this.statusJob = statusJob;
    }

    public void setHighPriority(int highPriority) {
        this.highPriority = highPriority;
    }

    @NonNull
    @Override
    public String toString() {
        return "Job{" +
                "idJob='" + idJob + '\'' +
                ", nameJob='" + nameJob + '\'' +
                ", timeStartDate=" + timeStartDate +
                ", timeEndDate=" + timeEndDate +
                ", highPriority=" + highPriority +
                '}';
    }


}
