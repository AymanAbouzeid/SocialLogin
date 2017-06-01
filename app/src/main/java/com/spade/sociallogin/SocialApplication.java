package com.spade.sociallogin;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Ayman Abouzaid on 5/30/17.
 */

public class SocialApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);
    }
}
