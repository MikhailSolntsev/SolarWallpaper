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

    private AsyncTask<Void, Void, Void> mWallapaperTask;

    //@Inject
    //DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //SolarApplication.getComponent().inject(this);
    }

    @Override
    public boolean onStartJob(final JobParameters job) {

        final DataManager dataManager = new DataManager(this);
        dataManager.getBitmapFromSdoObservable()
                .subscribe(bmp -> {
                    if (bmp != null) {
                        try {
                            dataManager.setWallpaper(bmp);
                            dataManager.showNotification(1);
                        } catch (IOException e) {
                            dataManager.showErrorNotification(e);
                            e.printStackTrace();
                        }
                    }

                    jobFinished(job, false);
                });
//        mWallapaperTask = new JobTask(this, job);
//
//        mWallapaperTask.execute();

        return true;

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mWallapaperTask != null) {
            mWallapaperTask.cancel(true);
        }
        return true;
    }

    public static boolean scheduleJob(boolean active, int interval) {
        GooglePlayDriver driver = new GooglePlayDriver(SolarApplication.getInstance());
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        if (interval == -1) {
            dispatcher.cancel(JOB_TAG);
            return true;
        }
        if (!active) {
            dispatcher.cancel(JOB_TAG);
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

    private static class JobTask extends AsyncTask<Void, Void, Void> {
        private JobService mJobService;
        private JobParameters mJob;

        public JobTask(JobService jobService, JobParameters job) {
            mJobService = jobService;
            mJob = job;
        }

        @Override
        protected Void doInBackground(Void[] params) {
            DataManager dataManager = new DataManager(mJobService);
            Bitmap bmp = null;
            try {
                bmp = dataManager.getBitmapFromSdoSync();
            } catch (Exception ex) {
                dataManager.showErrorNotification(ex);
                return null;
            } catch (Error er) {
                dataManager.showErrorNotification(er);
                return null;
            }
            if (bmp == null) {
                Throwable throwable = new NullPointerException("bmp is null");
                dataManager.showErrorNotification(throwable);
                //throw throwable;
            }

            //Point size = dataManager.getScreenSize();
            Point size = new Point(1280, 768);
            bmp = BitmapService.fitBitmapToSize(bmp, size.y, size.x);

            try {
                dataManager.setWallpaper(bmp);
                dataManager.showNotification(1);
            } catch (IOException e) {
                dataManager.showErrorNotification(e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            mJobService.jobFinished(mJob, false);
        }
    }

}
