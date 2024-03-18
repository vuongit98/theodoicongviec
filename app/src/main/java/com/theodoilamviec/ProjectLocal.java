package com.theodoilamviec;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ProjectLocal")
public class ProjectLocal {

    @PrimaryKey(autoGenerate = true)
     public Long id ;
    public String idProject;
    public String nameProject;
    public String nameCreator;

    public ProjectLocal() {
    }

    public ProjectLocal(String idProject, String nameProject, String nameCreator) {
        this.idProject = idProject;
        this.nameProject = nameProject;
        this.nameCreator = nameCreator;
    }

    public ProjectLocal(Long id, String idProject, String nameProject, String nameCreator) {
        this.id = id;
        this.idProject = idProject;
        this.nameProject = nameProject;
        this.nameCreator = nameCreator;
    }

}
