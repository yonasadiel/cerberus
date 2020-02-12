package com.yonasadiel.cerberus.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.yonasadiel.cerberus.LockApplication;
import com.yonasadiel.cerberus.LockScreenActivity;
import com.yonasadiel.cerberus.CerberusActivity;
import com.yonasadiel.cerberus.R;


public class LockScreenService extends Service {
    private Context mContext = null;
    private NotificationManager mNM;

    private BroadcastReceiver mLockScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != context && null != intent.getAction()) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    startLockScreenActivity();
                }
            }
        }
    };

    private void stateReceiver(boolean isStartReceiver) {
        if (isStartReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mLockScreenReceiver, filter);
        } else {
            if (null != mLockScreenReceiver) {
                unregisterReceiver(mLockScreenReceiver);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stateReceiver(true);
        return LockScreenService.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stateReceiver(false);
        mNM.cancel(((LockApplication) getApplication()).notificationId);
    }

    private void startLockScreenActivity() {
        Intent startLockScreenActIntent = new Intent(mContext, LockScreenActivity.class);
        startLockScreenActIntent.setAction(Intent.ACTION_VIEW);
        startLockScreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startLockScreenActIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(startLockScreenActIntent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    private void showNotification() {
        CharSequence text = "Running";
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, CerberusActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.cerberus)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .build();

        mNM.notify(((LockApplication) getApplication()).notificationId, notification);
    }

}