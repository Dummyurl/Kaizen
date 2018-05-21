package com.kaizen.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaizen.R;


public class ToastUtil {
    public static void showInfo(Activity activity, int msgId) {
        if (activity != null && !activity.isFinishing()) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.item_toast, (ViewGroup) activity.findViewById(R.id.cv_toast));

            TextView text = view.findViewById(R.id.tv_message);
            text.setText(msgId);

            Toast toast = new Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }

    public static void showInfo(Activity activity, String msgId) {
        if (activity != null && !activity.isFinishing()) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.item_toast, (ViewGroup) activity.findViewById(R.id.cv_toast));

            TextView text = view.findViewById(R.id.tv_message);
            text.setText(msgId);

            Toast toast = new Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }

    public static void showError(Activity activity, int msgId) {
        if (activity != null && !activity.isFinishing()) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.item_toast, (ViewGroup) activity.findViewById(R.id.cv_toast));

            CardView cv_toast = view.findViewById(R.id.cv_toast);
            cv_toast.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.toast_warn));

            ImageView iv_icon = view.findViewById(R.id.iv_icon);
            iv_icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_warn));

            TextView text = view.findViewById(R.id.tv_message);
            text.setText(msgId);

            Toast toast = new Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }

    public static void showError(Activity activity, String msgId) {
        if (activity != null && !activity.isFinishing()) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.item_toast, (ViewGroup) activity.findViewById(R.id.cv_toast));

            CardView cv_toast = view.findViewById(R.id.cv_toast);
            cv_toast.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.toast_warn));

            ImageView iv_icon = view.findViewById(R.id.iv_icon);
            iv_icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_warn));

            TextView text = view.findViewById(R.id.tv_message);
            text.setText(msgId);

            Toast toast = new Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }

    public static void showSuccess(Activity activity, int msgId) {
        if (activity != null && !activity.isFinishing()) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.item_toast, (ViewGroup) activity.findViewById(R.id.cv_toast));

            CardView cv_toast = view.findViewById(R.id.cv_toast);
            cv_toast.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.toast_success));

            ImageView iv_icon = view.findViewById(R.id.iv_icon);
            iv_icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_check));

            TextView text = view.findViewById(R.id.tv_message);
            text.setText(msgId);

            Toast toast = new Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }

    public static void showSuccess(Activity activity, String msgId) {
        if (activity != null && !activity.isFinishing()) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.item_toast, (ViewGroup) activity.findViewById(R.id.cv_toast));

            CardView cv_toast = view.findViewById(R.id.cv_toast);
            cv_toast.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.toast_success));

            ImageView iv_icon = view.findViewById(R.id.iv_icon);
            iv_icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_check));

            TextView text = view.findViewById(R.id.tv_message);
            text.setText(msgId);

            Toast toast = new Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }
}
