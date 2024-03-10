package com.theodoilamviec.Account;

public class JobUser {
    private String idJob ;
    private String idJobUser ;
    private User user ;

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
}
