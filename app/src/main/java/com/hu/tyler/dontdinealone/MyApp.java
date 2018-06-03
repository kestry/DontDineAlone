package com.hu.tyler.dontdinealone;

import android.app.Application;
import android.content.Context;

/*
 * This class is an extention of Application and was created so that we can get
 * the package name from anywhere. The Manifest was changed so that the Application name
 * refers to this class.
 * Please note that if we move or change this file to a different folder, we will likely
 * have to update the Manifest as well.
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