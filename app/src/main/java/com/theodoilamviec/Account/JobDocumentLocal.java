package com.theodoilamviec.Account;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "JobDocumentLocal")
public class JobDocumentLocal {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String idJob;
    public  String idJobDocument;
    public String nameDocument;
    public String linkDocument;
    public String idProject;

    public JobDocumentLocal(String idJob, String idJobDocument, String nameDocument, String linkDocument, String idProject) {
        this.idJob = idJob;
        this.idJobDocument = idJobDocument;
        this.nameDocument = nameDocument;
        this.linkDocument = linkDocument;
        this.idProject = idProject;
    }
}
