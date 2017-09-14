package com.debugg3r.android.solarwallpaper.dagger;

import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenterImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {
    @Provides
    @ApplicationScope
    @Inject
    public MainPresenter provideMainPresenter(DataManager dataManager) {
        return new MainPresenterImpl(dataManager);
    }
}
