package com.catasoft.ip_finder.ui.helpers;

import android.app.Application;

public class BaseApp extends Application {
    private static BaseApp baseApp;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
    }

    public static BaseApp getInstance() {return baseApp;}
}
