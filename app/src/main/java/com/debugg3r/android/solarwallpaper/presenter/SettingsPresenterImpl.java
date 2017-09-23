package com.debugg3r.android.solarwallpaper.presenter;

import android.graphics.Point;
import android.provider.ContactsContract;

import com.debugg3r.android.solarwallpaper.model.BitmapService;
import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.model.WallpaperJobService;
import com.debugg3r.android.solarwallpaper.view.SettingsView;

import java.io.IOException;

import rx.schedulers.Schedulers;

public class SettingsPresenterImpl implements SettingsPresenter {

    private SettingsView mView;
    private DataManager mDataManager;

    public SettingsPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void scheduleWallpaperUpdate(boolean active, String freq) {
        // schedule job
        int interval;
        try {
            interval = Integer.valueOf(freq);
        } catch (NumberFormatException ex) {
            mView.showToast("Interval format incorrect");
            return;
        }
        WallpaperJobService.scheduleJob(active, interval);
        if (mView != null) {
            mView.showToast("Wallpaper updater scheduled");
        }
    }

    @Override
    public void attachView(SettingsView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void updateWallpaper() {
        mDataManager.getBitmapFromSdo()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(bmp -> {
                    Point size = mDataManager.getScreenSize();
                    bmp = BitmapService.fitBitmapToSize(bmp, size.y, size.x);
                    return bmp;
                })
                .subscribe(bmp -> {
                    try {
                        mDataManager.setWallpaper(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                        mView.showToast("Can't set wallpaper due to error " + e.getMessage());
                    }
                });
    }

}
