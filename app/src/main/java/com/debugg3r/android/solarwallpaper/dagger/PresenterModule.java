package com.debugg3r.android.solarwallpaper.dagger;

import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {
    @Provides
    @ApplicationScope
    public MainPresenter provideMainPresenter() {
        return new MainPresenter();
    }
}
