package com.theodoilamviec;

import android.content.Context;

public class SingletonSharePreferences {

    private SharePreferencesUtil sharePreferencesUtil ;
    private static SingletonSharePreferences singletonSharePreferences ;

    void init(Context context){
        sharePreferencesUtil = new SharePreferencesUtil();
        sharePreferencesUtil.init(context);
    }
    public static SingletonSharePreferences getInstance(){
        if (singletonSharePreferences == null) {
            singletonSharePreferences = new SingletonSharePreferences();
        }
        return singletonSharePreferences;
    }

    public void putJobNotification(String idJob , boolean data) {
        sharePreferencesUtil.putJobNotification(idJob, data);
    }

    public boolean getJobNotification(String idJob) {
        return sharePreferencesUtil.getJobNotification(idJob);
    }

}
