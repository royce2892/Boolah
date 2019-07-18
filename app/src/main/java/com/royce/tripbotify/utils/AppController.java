package com.royce.tripbotify.utils;

import android.app.Application;

import io.realm.Realm;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
