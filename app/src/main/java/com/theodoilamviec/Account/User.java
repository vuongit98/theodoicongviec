package com.theodoilamviec.Account;

import androidx.annotation.NonNull;

public class User {
    private String uid ;
    private String userName ;
    private String password ;

    public User() {
    }

    public User(String uid, String userName, String password) {
        this.uid = uid;
        this.userName = userName;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return userName+"-"+uid ;
    }
}
