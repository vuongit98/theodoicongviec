package com.theodoilamviec.Account;

import android.content.Context;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class UserModel implements Serializable {
    private  int id;
    private String email;
    private String pass;
    private FirebaseAuth firebaseAuth;
    private String valid_email = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private IUser callback;
    private String hoten;
    private String avatar;
    private double chieucao;
    private double cannang;
    private String gioitinh;
    private String date_of_birth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public UserModel(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public UserModel(IUser callback, Context context) {
        this.callback = callback;
        firebaseAuth = FirebaseAuth.getInstance();


    }

    public UserModel() {
    }


    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getChieucao() {
        return chieucao;
    }

    public void setChieucao(double chieucao) {
        this.chieucao = chieucao;
    }

    public double getCannang() {
        return cannang;
    }

    public void setCannang(double cannang) {
        this.cannang = cannang;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void HandleLoginUser(String email, String pass) {
        if (email.length() > 0) {
            if (pass.length() > 0) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                callback.OnSucess();
                            } else {
                                callback.OnAuthEmail();
                            }

                        } else {
                            callback.OnFail();
                        }
                    }
                });
            } else {
                callback.OnPass();
            }

        } else {
            callback.OnEmptyEmail();
        }

    }

    public void HandleRegistUser(String email, String pass, String pass1) {
        if (email.length() > 0) {
            if (pass.length() > 0) {
                if (pass.equals(pass1)) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String uid = firebaseAuth.getCurrentUser().getUid();
                                firebaseDatabase.getReference("Users").child(uid)
                                                .setValue(new User(uid,email,pass));
                                firebaseAuth.getCurrentUser().sendEmailVerification();
                                callback.OnSucess();

                            } else {
                                callback.OnFail();
                            }
                        }
                    });
                } else {
                    callback.OnpassSame();
                }

            } else {
                callback.OnPass();
            }

        } else {
            callback.OnEmptyEmail();
        }

    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
