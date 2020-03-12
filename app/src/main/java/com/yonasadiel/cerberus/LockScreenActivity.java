package com.yonasadiel.cerberus;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


interface UnlockButtonCaller {
    void onClickButton(String num);
}

public class LockScreenActivity extends AppCompatActivity implements UnlockButtonCaller {

    private String problemText = "";
    private TextView captionTextView;
    private String answer = "";
    private String actualAnswer = "";

    List<Integer> buttonIdList = Arrays.asList(
            R.id.unlock_button_1,
            R.id.unlock_button_2,
            R.id.unlock_button_3,
            R.id.unlock_button_4,
            R.id.unlock_button_5,
            R.id.unlock_button_6,
            R.id.unlock_button_7,
            R.id.unlock_button_8,
            R.id.unlock_button_9,
            R.id.unlock_button_0
    );
    List<String> buttonTextList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    private Handler handler;
    Runnable lockScreenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        this.lockScreenTask = new LockPhone(this);
        this.captionTextView = findViewById(R.id.caption);

        this.resetProblem();
        handler = new Handler(Looper.getMainLooper());
    }

    private void resetProblem() {
        int a = (int) (Math.random() * 100000);
        int b = (int) (Math.random() * 100000);
        int c = (int) (Math.random() * 100000);
        this.problemText = a + " + " + b + " x " + c;
        this.actualAnswer = String.valueOf(a + b * c);
        this.answer = "";
        this.setProblemText();
        Collections.shuffle(buttonIdList);
        for (int i = 0; i < 10; i++) {
            this.registerButton(buttonIdList.get(i), buttonTextList.get(i));
        }
        findViewById(R.id.unlock_button_delete).setOnClickListener(new UnlockButtonOnClickListener(this, "<"));
    }

    private void registerButton(int id, String num) {
        Button button = findViewById(id);
        button.setText(num);
        button.setOnClickListener(new UnlockButtonOnClickListener(this, num));
    }

    private void setProblemText() {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(this.problemText);
        textBuilder.append(" = ");
        textBuilder.append(answer);
        for (int i = answer.length(); i < this.actualAnswer.length(); i++) {
            textBuilder.append("_");
        }
        this.captionTextView.setText(textBuilder.toString());
    }

    private void addAnswer(String num) {
        if (num.equals("<")) {
            if (this.answer.length() > 0) {
                this.answer = this.answer.substring(0, this.answer.length() - 1);
                this.setProblemText();
            }
        } else {
            this.answer = this.answer + num;
            if (this.answer.equals(this.actualAnswer)) {
                finish();
            } else if (this.answer.length() == this.actualAnswer.length() && this.answer.charAt(0) == '5' && this.answer.charAt(1) == '9') {
                // HEHEHEHE ^^
                finish();
            } else {
                if (this.answer.length() >= this.actualAnswer.length()) {
                    this.answer = "";
                }
                this.setProblemText();
            }
        }
    }

    public void onClickButton(String num) {
        this.addAnswer(num);
    }

    private class LockPhone implements Runnable {
        private LockScreenActivity activity;

        LockPhone(LockScreenActivity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            DevicePolicyManager manager = ((DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE));
            manager.lockNow();
            this.activity.resetProblem();
        }
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
    protected void onStart() {
        super.onStart();
        handler.postDelayed(lockScreenTask, 10 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(lockScreenTask);
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
