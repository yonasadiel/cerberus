package com.yonasadiel.cerberus;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.yonasadiel.cerberus.utils.LockDeviceAdminReceiver;
import com.yonasadiel.cerberus.utils.LockScreen;

public class CerberusActivity extends AppCompatActivity {
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String packageName = this.getPackageName();
        findViewById(R.id.open_apps_detail_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", packageName, null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        findViewById(R.id.open_device_administration_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName name = new ComponentName(getApplicationContext(), LockDeviceAdminReceiver.class);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, name);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,  R.string.permission_device_admin_description);
                startActivity(intent);
            }
        });


        toggleButton = findViewById(R.id.toggleButton);
        LockScreen.getInstance().init(this,true);
        if (LockScreen.getInstance().isActive()) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LockScreen.getInstance().active();
                } else {
                    LockScreen.getInstance().deactivate();
                }
            }
        });
    }
}

