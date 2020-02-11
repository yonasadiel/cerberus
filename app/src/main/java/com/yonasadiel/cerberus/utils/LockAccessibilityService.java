package com.yonasadiel.cerberus.utils;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.yonasadiel.cerberus.LockApplication;

public class LockAccessibilityService extends AccessibilityService {

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        LockScreen.getInstance().init(this);
        if(  ((LockApplication) getApplication()).lockScreenShow ){
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.KEYCODE_HOME
                    || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                    || keyCode == KeyEvent.KEYCODE_MENU) {
                return true;
            }
        }

        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        //
    }

    @Override
    public void onInterrupt() {

    }

}

