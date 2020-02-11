package com.yonasadiel.cerberus;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.yonasadiel.cerberus.utils.LockScreen;

public class CerberusActivity extends AppCompatActivity {
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton = findViewById(R.id.toggleButton);
        LockScreen.getInstance().init(this,true);
        if (LockScreen.getInstance().isActive()) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }

        toggleButton.setOnCheckedChangeListener(new ToggleButtonOnCheckedChangeListener(this));
    }

    public void openApplicationDetailsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    class ToggleButtonOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private CerberusActivity activity;

        ToggleButtonOnCheckedChangeListener(CerberusActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            if (checked) {
                LockScreen.getInstance().active();
                this.activity.openApplicationDetailsSettings();
            } else {
                LockScreen.getInstance().deactivate();
            }
        }
    }
}

