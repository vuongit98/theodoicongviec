package com.theodoilamviec.Account;

public interface IUser {
    void OnSucess();

    void OnFail();

    void OnValidEmail();

    void OnEmptyEmail();

    void OnPass();

    void OnpassSame();

    void OnAuthEmail();

    void OnEmpty();

    void getProfileUser(int iduser,String hoten, String avatar, double chieucao, double cannang, String gioitinh, String date_of_birth);
}
