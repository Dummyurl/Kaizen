package com.kaizen.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.kaizen.models.User;

/**
 * Created by FAMILY on 14-12-2017.
 */

public class PreferenceUtil {
    public static User getUser(Context context) {
        String value = PreferenceManager.getDefaultSharedPreferences(context).getString("user", null);
        User user = null;

        if (value != null) {
            user = new Gson().fromJson(value, User.class);
        }

        return user;
    }

    public static void setUser(Context context, User user) {
        String value = new Gson().toJson(user);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user", value).apply();
    }

    public static void setLanguage(Context context, int language) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("language", language).apply();
    }

    public static int getLanguage(Context context) {
        if (context != null) {
            return PreferenceManager.getDefaultSharedPreferences(context).getInt("language", 1);
        }
        return 1;
    }
}
