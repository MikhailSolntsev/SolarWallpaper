package com.debugg3r.android.solarwallpaper;

import android.app.Application;

import com.debugg3r.android.solarwallpaper.dagger.ApplicationComponent;
import com.debugg3r.android.solarwallpaper.dagger.ApplicationModule;
import com.debugg3r.android.solarwallpaper.dagger.DaggerApplicationComponent;
import com.debugg3r.android.solarwallpaper.dagger.DataModule;
import com.debugg3r.android.solarwallpaper.dagger.PresenterModule;

public class SolarApplication extends Application {
    private static SolarApplication instance;
    private static ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .presenterModule(new PresenterModule())
                .dataModule(new DataModule())
                .build();
        instance = this;
    }

    public static SolarApplication getInstance() {
        return instance;
    }

    public static ApplicationComponent getComponent() {
        return mComponent;
    }
}
