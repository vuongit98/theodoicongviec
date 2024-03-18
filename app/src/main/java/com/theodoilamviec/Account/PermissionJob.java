package com.theodoilamviec.Account;

import androidx.room.Entity;

@Entity(tableName = "PermissionJob")
public class PermissionJob {
    private String idPermission;
    private String uid ;
    private String idJob ;

    private String idProject ;

    public PermissionJob(String idPermission, String uid, String idJob, String idProject) {
        this.idPermission = idPermission;
        this.uid = uid;
        this.idJob = idJob;
        this.idProject = idProject;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public PermissionJob() {
    }
    public PermissionJob(String idPermission, String uid, String idJob) {
        this.idPermission = idPermission;
        this.uid = uid;
        this.idJob = idJob;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(String idPermission) {
        this.idPermission = idPermission;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
