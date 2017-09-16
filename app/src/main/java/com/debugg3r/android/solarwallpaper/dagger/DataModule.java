package com.debugg3r.android.solarwallpaper.dagger;

import android.content.Context;

import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.model.SharedPreferencesHelper;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Provides
    @Inject
    public DataManager provideDataModule(@ApplicationContext Context context){
        return new DataManager(context);
    }

    @Provides
    @Inject
    public SharedPreferencesHelper provideSharedPreferencesHelper(@ApplicationContext Context context) {
        return new SharedPreferencesHelper(context);
    }
}
