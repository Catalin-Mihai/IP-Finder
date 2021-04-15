package com.catasoft.ip_finder.ui.helpers;

import android.app.Application;

import com.catasoft.ip_finder.data.room.AppRoomDatabase;

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //AppRoomDatabase.getDatabaseInstance(this);
    }
}
