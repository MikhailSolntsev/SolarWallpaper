package com.debugg3r.android.solarwallpaper.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.debugg3r.android.solarwallpaper.view.MainActivity;

public class SharedPreferencesHelper {
    private Context mContext;

    public SharedPreferencesHelper(Context context) {
        this.mContext = context;
    }

    public void putInt(String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public void putString(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(key, value)
                .apply();
    }

    public void putBoolean(String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public int getInt(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (preferences.contains(key)) {
            return preferences.getInt(key, 0);
        }
        return 0;
    }

    public String getString(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (preferences.contains(key)) {
            return preferences.getString(key, "");
        }
        return "";
    }

    public boolean getBoolean(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (preferences.contains(key)) {
            return preferences.getBoolean(key, false);
        }
        return false;
    }

    public void registerOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
