package com.theodoilamviec.theodoilamviec.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notification")
public class Notification implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;
    @ColumnInfo(name = "title")
    public String nameJob;
    @ColumnInfo(name = "obj_id")
    public String idJob;

    public Notification(String id, String nameJob, String idJob) {
        this.id = id;
        this.nameJob = nameJob;
        this.idJob = idJob;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNameJob() {
        return nameJob;
    }

    public void setNameJob(String nameJob) {
        this.nameJob = nameJob;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }
}
