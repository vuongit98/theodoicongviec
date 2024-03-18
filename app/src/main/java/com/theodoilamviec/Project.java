package com.theodoilamviec;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

public class Project implements Parcelable {
    private String idProject;
    private String nameProject;
    private String nameCreator;

    public Project(String idProject, String nameProject, String nameCreator) {
        this.idProject = idProject;
        this.nameProject = nameProject;
        this.nameCreator = nameCreator;
    }

    public Project() {
    }

    protected Project(Parcel in) {
        idProject = in.readString();
        nameProject = in.readString();
        nameCreator = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getNameCreator() {
        return nameCreator;
    }

    public void setNameCreator(String nameCreator) {
        this.nameCreator = nameCreator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idProject);
        dest.writeString(nameProject);
        dest.writeString(nameCreator);
    }
}
