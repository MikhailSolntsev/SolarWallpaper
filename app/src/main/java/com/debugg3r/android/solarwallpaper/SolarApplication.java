package com.debugg3r.android.solarwallpaper;

import android.app.Application;

import com.debugg3r.android.solarwallpaper.dagger.ApplicationComponent;
import com.debugg3r.android.solarwallpaper.dagger.DaggerApplicationComponent;
import com.debugg3r.android.solarwallpaper.dagger.PresenterModule;

public class SolarApplication extends Application {
    private static Application instance;
    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent
                .builder()
                .presenterModule(new PresenterModule())
                .build();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    public static ApplicationComponent getComponent() {
        return component;
    }
}
