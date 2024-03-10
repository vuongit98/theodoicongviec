package com.theodoilamviec.Account;

public class GroupUser {
    private String idGroup ;
    private String uidPerson ;
    private String idGroupUser ;

    public GroupUser() {
    }

    public GroupUser(String idGroup, String uidPerson, String idGroupUser) {
        this.idGroup = idGroup;
        this.uidPerson = uidPerson;
        this.idGroupUser = idGroupUser;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getUidPerson() {
        return uidPerson;
    }

    public void setUidPerson(String uidPerson) {
        this.uidPerson = uidPerson;
    }

    public String getIdGroupUser() {
        return idGroupUser;
    }

    public void setIdGroupUser(String idGroupUser) {
        this.idGroupUser = idGroupUser;
    }
}
