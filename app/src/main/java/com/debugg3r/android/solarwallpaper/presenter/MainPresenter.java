package com.debugg3r.android.solarwallpaper.presenter;

import android.support.annotation.NonNull;

import com.debugg3r.android.solarwallpaper.view.MainView;

public interface MainPresenter {

    void attachView(@NonNull MainView view);

    void deattachView();

    void loadCurrentImage();

    void scheduleJob();

    void setWallpaper();
}
