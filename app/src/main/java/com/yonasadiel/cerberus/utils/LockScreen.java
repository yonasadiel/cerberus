package com.yonasadiel.cerberus.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;


public class LockScreen {
    private Context context;
    private static LockScreen singleton;
    private boolean disableHomeButton = false;

    public static LockScreen getInstance() {
        if (singleton == null) {
            singleton = new LockScreen();
        }
        return singleton;
    }

    void init(Context context) {
        this.context = context;
    }

    public void init(Context context, boolean disableHomeButton){
        this.context = context;
        this.disableHomeButton = disableHomeButton;
    }

    private void showSettingAccessibility(){
        if(!isMyServiceRunning(LockAccessibilityService.class)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);
        }
    }

    public void active(){
        if (disableHomeButton) {
            showSettingAccessibility();
        }

        if (context!=null) {
            context.startService(new Intent(context, LockScreenService.class));
        }
    }

    public void deactivate(){
        if(context!=null) {
            context.stopService(new Intent(context, LockScreenService.class));
        }
    }
    public boolean isActive(){
        if(context!=null) {
            return isMyServiceRunning(LockScreenService.class);
        }else{
            return false;
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
