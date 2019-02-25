package com.aslansari.rxjavastatemng;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import java.io.File;

import timber.log.Timber;

public class StateManageApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static StateManageApplication currentApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();

        currentApplication = this;

        initializeTimber();
    }

    public static StateManageApplication getInstance() {
        return currentApplication;
    }

    public static File getCacheDirectory()  {
        return currentApplication.getCacheDir();
    }

    private void initializeTimber(){
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                // Add the line number to the tag
                @Override
                protected String createStackElementTag(@NonNull StackTraceElement element) {
                    return String.format("[Line - %s] [Method - %s] [Class - %s]",
                            element.getLineNumber(),
                            element.getMethodName(),
                            super.createStackElementTag(element));
                }
            });
        }
    }

}
