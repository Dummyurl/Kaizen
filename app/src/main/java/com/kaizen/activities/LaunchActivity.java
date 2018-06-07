package com.kaizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kaizen.models.User;
import com.kaizen.utils.PreferenceUtil;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    User user = PreferenceUtil.getUser(LaunchActivity.this);

                    if (user == null) {
                        startLogin();
                    } else {
                        startHome();
                    }
                }
            }, 3000);

        } catch (Exception e) {
            logError(e);
        }
    }

    private void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
