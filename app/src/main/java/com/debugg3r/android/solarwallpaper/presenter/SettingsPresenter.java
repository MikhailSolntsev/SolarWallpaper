package com.debugg3r.android.solarwallpaper.presenter;

import com.debugg3r.android.solarwallpaper.view.SettingsView;

public interface SettingsPresenter {

    void scheduleWallpaperUpdate(boolean active, String freq);

    void attachView(SettingsView view);

    void detachView();

    void updateWallpaper();

}
