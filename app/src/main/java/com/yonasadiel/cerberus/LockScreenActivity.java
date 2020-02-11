package com.yonasadiel.cerberus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


interface UnlockButtonCaller {
    void onClickButton(String num);
}

public class LockScreenActivity extends AppCompatActivity implements UnlockButtonCaller {

    private String problemText = "";
    private TextView captionTextView;
    private String answer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        this.captionTextView = findViewById(R.id.caption);
        this.problemText = ((int) (Math.random() * 100000))
                + " + " + ((int) (Math.random() * 100000))
                + " x " + ((int) (Math.random() * 100000));
        this.answer = "";
        this.setProblemText();

        findViewById(R.id.unlock_button_1).setOnClickListener(new UnlockButtonOnClickListener(this, "1"));
        findViewById(R.id.unlock_button_2).setOnClickListener(new UnlockButtonOnClickListener(this, "2"));
        findViewById(R.id.unlock_button_3).setOnClickListener(new UnlockButtonOnClickListener(this, "3"));
        findViewById(R.id.unlock_button_4).setOnClickListener(new UnlockButtonOnClickListener(this, "4"));
        findViewById(R.id.unlock_button_5).setOnClickListener(new UnlockButtonOnClickListener(this, "5"));
        findViewById(R.id.unlock_button_6).setOnClickListener(new UnlockButtonOnClickListener(this, "6"));
        findViewById(R.id.unlock_button_7).setOnClickListener(new UnlockButtonOnClickListener(this, "7"));
        findViewById(R.id.unlock_button_8).setOnClickListener(new UnlockButtonOnClickListener(this, "8"));
        findViewById(R.id.unlock_button_9).setOnClickListener(new UnlockButtonOnClickListener(this, "9"));
        findViewById(R.id.unlock_button_0).setOnClickListener(new UnlockButtonOnClickListener(this, "0"));
        findViewById(R.id.unlock_button_delete).setOnClickListener(new UnlockButtonOnClickListener(this, "<"));
    }

    private void setProblemText() {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(this.problemText);
        textBuilder.append(" = ");
        textBuilder.append(answer);
        for (int i = answer.length(); i < 10; i++) {
            textBuilder.append("_");
        }
        this.captionTextView.setText(textBuilder.toString());
    }

    private void addAnswer(String num) {
        Log.d("CerberusLog", this.answer + " " + num);
        if (num.equals("<")) {
            if (this.answer.length() > 0) {
                this.answer = this.answer.substring(0, this.answer.length() - 1);
                this.setProblemText();
            }
        } else {
            this.answer = this.answer + num;
            if (this.answer.length() == 10 && this.answer.charAt(this.answer.length() - 1) == '9' && this.answer.charAt(0) == '9') {
                finish();
            } else {
                if (this.answer.length() >= 10) {
                    this.answer = "";
                }
                this.setProblemText();
            }
        }
    }

    public void onClickButton(String num) {
        this.addAnswer(num);
    }

    private class UnlockButtonOnClickListener implements View.OnClickListener {
        private UnlockButtonCaller caller;
        private String num;

        UnlockButtonOnClickListener(UnlockButtonCaller caller, String num) {
            this.caller = caller;
            this.num = num;
        }

        @Override
        public void onClick(View v) {
            this.caller.onClickButton(this.num);
        }
    }

    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onAttachedToWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((LockApplication) getApplication()).lockScreenShow = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((LockApplication) getApplication()).lockScreenShow = false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
}
