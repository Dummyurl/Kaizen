package com.kaizen.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kaizen.utils.ToastUtil;


/**
 * Created by smeesala on 6/30/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    public void showInfoToast(int msg) {
        ToastUtil.showInfo(this, msg);
    }

    public void showErrorToast(int msg) {
        ToastUtil.showError(this, msg);
    }

    public void showErrorToast(String msg) {
        ToastUtil.showError(this, msg);
    }

    public void showSuccessToast(int msg) {
        ToastUtil.showSuccess(this, msg);
    }

    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.e(TAG, "isNetworkAvailable() - " + e.toString(), e);
        }
        return false;
    }

    public void logError(Exception exception) {
    }

    public void logError(String exception) {
    }
}
