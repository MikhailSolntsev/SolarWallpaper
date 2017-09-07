package com.debugg3r.android.solarwallpaper.dagger;

import com.debugg3r.android.solarwallpaper.view.MainActivity;

import dagger.Component;

@ApplicationScope
@Component (modules = PresenterModule.class)
public interface ApplicationComponent {
    void inject(MainActivity activity);
}
