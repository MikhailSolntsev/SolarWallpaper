package com.debugg3r.android.solarwallpaper.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;

import com.debugg3r.android.solarwallpaper.SolarApplication;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WallpaperJobService extends JobService {

    private static String JOB_TAG = "SolarWallpaperUpdater";
    private static boolean isScheduled = false;

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SolarApplication.getComponent().inject(this);
    }

    @Override
    public boolean onStartJob(final JobParameters job) {
        if (dataManager == null) {
            dataManager = new DataManager(this);
        }

        //setWallpaperObservable(job);

        setWallpaperAsync(job);

        return true;

    }

    private void setWallpaperAsync(final JobParameters job) {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Bitmap bmp = dataManager.getBitmapFromSdoSync();
                if (bmp == null) {
                    Throwable throwable = new NullPointerException("bmp is null");
                    dataManager.showErrorNotification(throwable);
                    //throw throwable;
                }

                Point size = dataManager.getScreenSize();
                bmp = BitmapService.fitBitmapToSize(bmp, size.y, size.x);

                try {
                    dataManager.setWallpaper(bmp);
                } catch (IOException e) {
                    dataManager.showErrorNotification(e);
                    e.printStackTrace();
                }

                dataManager.showNotification(1);

                jobFinished(job, false);

                return null;
            }
        };

        task.execute();
    }

    private void setWallpaperObservable(final JobParameters job) {
        dataManager.getBitmapFromSdoObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(bmp -> {
                            Point size = dataManager.getScreenSize();
                            bmp = BitmapService.fitBitmapToSize(bmp, size.y, size.x);
                            return Observable.just(bmp);
                        }
                        , throwable -> Observable.create(s -> s.onError(throwable))
                        , () -> Observable.create(s -> s.onCompleted()))
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(bmp -> {
                            try {
                                dataManager.setWallpaper(bmp);
                            } catch (IOException e) {
                                dataManager.showErrorNotification(e);
                                e.printStackTrace();
                            }

                            dataManager.showNotification(1);
                            jobFinished(job, false);
                        }, throwable -> {
                            dataManager.showErrorNotification(throwable);
                            jobFinished(job, false);}
                        , () -> {
                            dataManager.showNotification(3);
                            jobFinished(job, false);
                        });
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public static boolean scheduleJob(boolean active, int interval) {
        GooglePlayDriver driver = new GooglePlayDriver(SolarApplication.getInstance());
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        if (interval == -1) {
            dispatcher.cancel(JOB_TAG);
            isScheduled = false;
            return true;
        }
        if (!active) {
            dispatcher.cancel(JOB_TAG);
            isScheduled = false;
            return true;
        }

        int scheduleInterval = (int) TimeUnit.MINUTES.toSeconds(interval);
        int scheduleFlextime = scheduleInterval / 3;

        Job wallpaperJob = dispatcher.newJobBuilder()
                .setService(WallpaperJobService.class)
                .setTag(JOB_TAG)
                .setRecurring(false)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setTrigger(Trigger.executionWindow(scheduleInterval, scheduleInterval + scheduleFlextime))
                .build();

        try {
            dispatcher.mustSchedule(wallpaperJob);
        } catch (FirebaseJobDispatcher.ScheduleFailedException ex) {
            return false;
        }
        return true;
    }
}
