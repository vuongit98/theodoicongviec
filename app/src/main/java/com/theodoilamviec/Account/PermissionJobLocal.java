package com.theodoilamviec.Account;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PermissionJobLocal")
public class PermissionJobLocal {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String idPermission;
    public  String uid;
    public  String idJob;
    public String idProject;

    public PermissionJobLocal(String idPermission, String uid, String idJob, String idProject) {
        this.idPermission = idPermission;
        this.uid = uid;
        this.idJob = idJob;
        this.idProject = idProject;
    }
}
