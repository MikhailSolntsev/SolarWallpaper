package com.debugg3r.android.solarwallpaper.dagger;

import android.content.Context;

import com.debugg3r.android.solarwallpaper.SolarApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private SolarApplication mApplication;

    public ApplicationModule(SolarApplication application) {
        this.mApplication = application;
    }

    @Provides
    @ApplicationContext
    public Context provideApplicationContext() {
        return mApplication;
    }
}
