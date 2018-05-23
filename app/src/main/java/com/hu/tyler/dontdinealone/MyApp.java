package com.hu.tyler.dontdinealone;

import android.app.Application;
import android.content.Context;

/*
 * This class is so that we can get the package from anywhere.
 * We changed the Manifest to reflect this in the Application name.
 *
 * Usage:
 *    String packageName = MyApp.getContext().getPackageName();
 *
 * Source:
 *    https://stackoverflow.com/questions/6589797/how-to-get-package-name-from-anywhere
 */
public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}