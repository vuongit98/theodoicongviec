package com.theodoilamviec;

import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.JobDocument;
import com.theodoilamviec.Account.JobNotification;
import com.theodoilamviec.Account.JobUser;
import com.theodoilamviec.Account.PermissionJob;

import java.util.List;

public interface ISyncData {

    void getSyncProject(List<Project> projectList) ;
    void getSyncJobs(List<Job> jobList);
    void getSyncJobNotifications(List<JobNotification> jobNotificationList);
    void getSyncJobDocuments(List<JobDocument> jobDocumentsList);
    void getSyncJobUser(List<JobUser> jobUserList);
    void getSyncPermissionJob(List<PermissionJob> permissionJobList);

}
