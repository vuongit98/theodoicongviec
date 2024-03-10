package com.theodoilamviec.Account;

public class Job {
    private String idJob = String.valueOf(System.currentTimeMillis());
    private String nameJob ;
    private Long timeStartDate ;
    private Long timeEndDate ;
    private int highPriority;
    public Job() {
    }

    public Job(String idJob, String nameJob, Long timeStartDate, Long timeEndDate, int highPriority) {
        this.idJob = idJob;
        this.nameJob = nameJob;
        this.timeStartDate = timeStartDate;
        this.timeEndDate = timeEndDate;
        this.highPriority = highPriority;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getNameJob() {
        return nameJob;
    }

    public void setNameJob(String nameJob) {
        this.nameJob = nameJob;
    }

    public Long getTimeStartDate() {
        return timeStartDate;
    }

    public void setTimeStartDate(Long timeStartDate) {
        this.timeStartDate = timeStartDate;
    }

    public Long getTimeEndDate() {
        return timeEndDate;
    }

    public void setTimeEndDate(Long timeEndDate) {
        this.timeEndDate = timeEndDate;
    }

    public int getHighPriority() {
        return highPriority;
    }

    public void setHighPriority(int highPriority) {
        this.highPriority = highPriority;
    }
}
