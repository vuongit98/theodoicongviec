package com.theodoilamviec.Account;

import android.content.Context;



public class UserController  implements  IUser{

    private UserModel userModel;
    private UserView callback;

    public UserController(UserView callback, Context context) {
        this.callback = callback;
        userModel = new UserModel(this,context);
    }
    public  void HandleLoginUser(String email,String pass){
         userModel.HandleLoginUser(email,pass);
    }
    public  void HandleRegistUser(String email,String pass,String repasst){
        userModel.HandleRegistUser(email,pass,repasst);
    }

    @Override
    public void OnSucess() {
        callback.OnSucess();

    }

    @Override
    public void OnFail() {
        callback.OnFail();

    }

    @Override
    public void OnValidEmail() {
        callback.OnValidEmail();

    }

    @Override
    public void OnEmptyEmail() {
        callback.OnEmptyEmail();

    }

    @Override
    public void OnPass() {
        callback.Onpass();

    }

    @Override
    public void OnpassSame() {
    callback.OnpassSame();
    }

    @Override
    public void OnAuthEmail() {
        callback.OnAuthEmail();
    }

    @Override
    public void OnEmpty() {
        callback.OnEmpty();

    }

    @Override
    public void getProfileUser(int iduser,String hoten, String avatar, double chieucao, double cannang, String gioitinh, String date_of_birth) {
      callback.getProfileUser(iduser,hoten,avatar,chieucao,cannang,gioitinh,date_of_birth);
    }
}
