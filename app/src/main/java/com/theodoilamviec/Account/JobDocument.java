package com.theodoilamviec.Account;

public class JobDocument {
    private String idJob;
    private String idJobDocument;
    private String nameDocument;
    private String linkDocument;

    public JobDocument(String idJob, String idJobDocument, String nameDocument, String linkDocument) {
        this.idJob = idJob;
        this.idJobDocument = idJobDocument;
        this.nameDocument = nameDocument;
        this.linkDocument = linkDocument;
    }

    public String getNameDocument() {
        return nameDocument;
    }

    public void setNameDocument(String nameDocument) {
        this.nameDocument = nameDocument;
    }

    public String getLinkDocument() {
        return linkDocument;
    }

    public void setLinkDocument(String linkDocument) {
        this.linkDocument = linkDocument;
    }

    public JobDocument() {
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getIdJobDocument() {
        return idJobDocument;
    }

    public void setIdJobDocument(String idJobDocument) {
        this.idJobDocument = idJobDocument;
    }
}
