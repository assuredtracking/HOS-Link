package com.hos.hoslink;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        try {
            super.onCreate();
            MyApplication.context = getApplicationContext();
        } catch (Exception e) {

        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);
    }

    public static Context GetAppContext() {
        return MyApplication.context;
    }
}
