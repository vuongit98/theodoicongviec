package com.theodoilamviec.Account;

public interface UserView {
    void OnSucess();

    void OnFail();

    void OnValidEmail();

    void OnEmptyEmail();

    void Onpass();

    void OnpassSame();

    void OnAuthEmail();

    void OnEmpty();

    void getProfileUser(int iduser,String hoten, String avatar, double chieucao, double cannang, String gioitinh, String date_of_birth);
}
