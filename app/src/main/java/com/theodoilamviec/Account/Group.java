package com.theodoilamviec.Account;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Group implements Parcelable {
    private String idGroup ;
    private String nameGroup ;

    public Group() {
    }

    public Group(String idGroup, String nameGroup, List<String> userList) {
        this.idGroup = idGroup;
        this.nameGroup = nameGroup;
    }

    protected Group(Parcel in) {
        idGroup = in.readString();
        nameGroup = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    @NonNull
    @Override
    public String toString() {
        return nameGroup ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idGroup);
        parcel.writeString(nameGroup);
    }
}
