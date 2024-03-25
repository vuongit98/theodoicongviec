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
    public String contentJob;
    public Long timeEnd ;

    public JobNotificationLocal() {
    }

    public String getContentJob() {
        return contentJob;
    }

    public void setContentJob(String contentJob) {
        this.contentJob = contentJob;
    }

    public JobNotificationLocal(String idUser, String idJob, String idProject, String nameJob, Long timeEnd,String contentJob) {
        this.idUser = idUser;
        this.idJob = idJob;
        this.idProject = idProject;
        this.nameJob = nameJob;
        this.timeEnd = timeEnd;
        this.contentJob = contentJob;
    }

    public JobNotificationLocal(String nameJob, String idJob, String idProject, Long timeEnd, String contentJob) {
        this.nameJob = nameJob;
        this.idJob = idJob;
        this.idProject = idProject;
        this.timeEnd = timeEnd;
        this.contentJob = contentJob;
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
