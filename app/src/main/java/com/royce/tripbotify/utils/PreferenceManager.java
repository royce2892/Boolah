package com.royce.tripbotify.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PreferenceManager {

    private SharedPreferences preferences;
    private static PreferenceManager sInstance;

    public PreferenceManager(Context context) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceManager(context);
        }
        return sInstance;
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void put(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void put(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putInSet(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> currentSet = getSet(key);
        currentSet.add(value);
        editor.putStringSet(key, currentSet);
        editor.apply();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public Set<String> getSet(String key) {
        return preferences.getStringSet(key, new HashSet<>());
    }

    public void increment(String key) {
        int curr = getInt(key);
        put(key, ++curr);
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }
}
