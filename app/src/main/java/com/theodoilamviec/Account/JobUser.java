package com.theodoilamviec.Account;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class JobUser implements Parcelable {
    private String idJob ;
    private String idJobUser ;
    private User user ;
    private String idProject ;

    public JobUser(String idJob, String idJobUser, User user, String idProject) {
        this.idJob = idJob;
        this.idJobUser = idJobUser;
        this.user = user;
        this.idProject = idProject;
    }

    protected JobUser(Parcel in) {
        idJob = in.readString();
        idJobUser = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        idProject = in.readString();
    }

    public static final Creator<JobUser> CREATOR = new Creator<JobUser>() {
        @Override
        public JobUser createFromParcel(Parcel in) {
            return new JobUser(in);
        }

        @Override
        public JobUser[] newArray(int size) {
            return new JobUser[size];
        }
    };

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public JobUser() {
    }

    public JobUser(String idJob, String idJobUser, User user) {
        this.idJob = idJob;
        this.idJobUser = idJobUser;
        this.user = user;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getIdJobUser() {
        return idJobUser;
    }

    public void setIdJobUser(String idJobUser) {
        this.idJobUser = idJobUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idJob);
        dest.writeString(idJobUser);
        dest.writeParcelable(user, flags);
        dest.writeString(idProject);
    }
}
