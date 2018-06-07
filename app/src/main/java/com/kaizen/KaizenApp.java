package com.kaizen;

import android.app.Application;
import android.content.Context;

import com.kaizen.utils.LocaleHelper;
import com.orm.SugarContext;

public class KaizenApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
