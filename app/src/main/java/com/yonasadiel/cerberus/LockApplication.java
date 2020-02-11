package com.yonasadiel.cerberus;

import android.app.Application;

public class LockApplication extends Application {

    public boolean lockScreenShow = false;
    public int notificationId = 1090;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
