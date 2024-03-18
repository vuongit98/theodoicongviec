package com.theodoilamviec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {
    private Context mContext ;
    private SharedPreferences sharedPreferences ;
    public void init(Context context){
        mContext = context;
        sharedPreferences = context.getSharedPreferences("MY_APP",Context.MODE_PRIVATE);

    }
    public void putJobNotification(String idJob, Boolean data){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(idJob,data);
    }

    public boolean getJobNotification(String idJob) {
        return sharedPreferences.getBoolean(idJob,false);
    }

}
