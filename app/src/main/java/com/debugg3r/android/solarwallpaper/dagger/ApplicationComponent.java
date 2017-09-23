package com.debugg3r.android.solarwallpaper.dagger;

import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;
import com.debugg3r.android.solarwallpaper.view.MainActivity;
import com.debugg3r.android.solarwallpaper.view.SettingsActivity;
import com.firebase.jobdispatcher.JobService;

import dagger.Component;

@ApplicationScope
@Component (modules = {PresenterModule.class, ApplicationModule.class, DataModule.class})
public interface ApplicationComponent {

    @ApplicationScope
    void inject(MainActivity activity);

    @ApplicationScope
    void inject(JobService jobService);

    @ApplicationScope
    void inject(SettingsActivity.SettingsFragment settingsActivity);
}
