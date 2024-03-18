package com.theodoilamviec.Account;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "JobLocal")
public class JobLocal implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String idJob = String.valueOf(System.currentTimeMillis());
    public String nameJob ;
    public Long timeStartDate ;
    public Long timeEndDate ;
    public int highPriority;
    public String idProject ;
    public   int statusJob = 0 ; // new:0 , responsing: 1 ,finished: 2, unfinished: 3
    public JobLocal() {
    }

    public JobLocal(String idJob, String nameJob, Long timeStartDate, Long timeEndDate, int highPriority, String idProject, int statusJob) {
        this.idJob = idJob;
        this.nameJob = nameJob;
        this.timeStartDate = timeStartDate;
        this.timeEndDate = timeEndDate;
        this.highPriority = highPriority;
        this.idProject = idProject;
        this.statusJob = statusJob;
    }

    public JobLocal(String idJob, String nameJob, Long timeStartDate, Long timeEndDate, int highPriority, String idProject) {
        this.idJob = idJob;
        this.nameJob = nameJob;
        this.timeStartDate = timeStartDate;
        this.timeEndDate = timeEndDate;
        this.highPriority = highPriority;
        this.idProject = idProject;
    }


    protected JobLocal(Parcel in) {
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<JobLocal> CREATOR = new Parcelable.Creator<JobLocal>() {
        @Override
        public JobLocal createFromParcel(Parcel in) {
            return new JobLocal(in);
        }

        @Override
        public JobLocal[] newArray(int size) {
            return new JobLocal[size];
        }
    };

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public JobLocal(String idJob, String nameJob, Long timeStartDate, Long timeEndDate, int highPriority) {
        this.idJob = idJob;
        this.nameJob = nameJob;
        this.timeStartDate = timeStartDate;
        this.timeEndDate = timeEndDate;
        this.highPriority = highPriority;
    }

    @NonNull
    @Override
    public String toString() {
        return "JobLocal{" +
                "idJob='" + idJob + '\'' +
                ", nameJob='" + nameJob + '\'' +
                ", timeStartDate=" + timeStartDate +
                ", timeEndDate=" + timeEndDate +
                ", highPriority=" + highPriority +
                '}';
    }

}
