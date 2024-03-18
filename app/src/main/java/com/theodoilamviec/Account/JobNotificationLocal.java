package com.theodoilamviec.Account;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "JobNotificationLocal")
public class JobNotificationLocal {
    @PrimaryKey(autoGenerate = true)
    public Long idJobNotification ;
    public String idUser ;
    public String idJob ;
    public String idProject ;
    public String nameJob;
    public Long timeEnd ;

    public JobNotificationLocal() {
    }

    public JobNotificationLocal( String idUser, String idJob, String idProject, String nameJob, Long timeEnd) {
        this.idUser = idUser;
        this.idJob = idJob;
        this.idProject = idProject;
        this.nameJob = nameJob;
        this.timeEnd = timeEnd;
    }

    public JobNotificationLocal(String nameJob, String idJob, String idProject, Long timeEnd) {
        this.nameJob = nameJob;
        this.idJob = idJob;
        this.idProject = idProject;
        this.timeEnd = timeEnd;
    }

    public Long getIdJobNotification() {
        return idJobNotification;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdJob() {
        return idJob;
    }

    public String getIdProject() {
        return idProject;
    }

    public String getNameJob() {
        return nameJob;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }
}
