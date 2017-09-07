package com.debugg3r.android.solarwallpaper.dagger;

import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;
import com.debugg3r.android.solarwallpaper.view.MainActivity;

import dagger.Component;

@ApplicationScope
@Component (modules = {PresenterModule.class, ApplicationModule.class, DataModule.class})
public interface ApplicationComponent {

    @ApplicationScope
    void inject(MainActivity activity);

}
