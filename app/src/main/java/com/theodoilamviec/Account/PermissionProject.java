package com.theodoilamviec.Account;

public class PermissionProject {
    private String idPermissionProject;
    private String uid ;
    private String idProject ;

    public PermissionProject(String idPermission, String uid, String idProject) {
        this.idPermissionProject = idPermission;
        this.uid = uid;
        this.idProject = idProject;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public PermissionProject() {
    }


    public String getIdPermission() {
        return idPermissionProject;
    }

    public void setIdPermission(String idPermission) {
        this.idPermissionProject = idPermission;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
