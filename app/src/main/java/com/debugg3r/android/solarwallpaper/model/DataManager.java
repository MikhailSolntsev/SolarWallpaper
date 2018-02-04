package com.debugg3r.android.solarwallpaper.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.view.WindowManager;

import com.debugg3r.android.solarwallpaper.R;
import com.debugg3r.android.solarwallpaper.model.loadservice.LoadServiceFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import rx.Observable;

public class DataManager {
    public static final int PENDING_INTENT_NOTIFICATION = 24092017;
    private static final String LOG_TAG = "SWP_DataManager";
    private final Context mContext;
    private SharedPreferencesHelper mSharedHelper;

    public DataManager(Context context) {
        this.mContext = context;

        mSharedHelper = new SharedPreferencesHelper(mContext);
    }

    public Bitmap getBitmapFromResource() {
        return BitmapService.getBitmapFromResource(mContext, R.drawable.latest);
    }

    public Observable<Bitmap> getBitmapFromSdoObservable() {
        String type = mSharedHelper.getString(mContext.getString(R.string.pref_image_type));
        String res = mSharedHelper.getString(mContext.getString(R.string.pref_image_resolution));
        Observable<Bitmap> bmpObservable = LoadServiceFactory.getLoadService().loadImageObservable(type, res);
        bmpObservable.doOnNext(bmp ->{
            OutputStream fout = null;
            File file = FileService.getOutputMediaFile(type);
            Log.d(LOG_TAG, "getBitmapFromSdoObservable: filename: " + file.getAbsolutePath());
            try {
                fout = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(LOG_TAG, "getBitmapFromSdoObservable: fout: " + fout);
            if (fout != null) {
                bmp.compress(Bitmap.CompressFormat.PNG, 95, fout);
                mSharedHelper.putString(mContext.getString(R.string.pref_stored_image), file.getAbsolutePath());
            }
            Log.d(LOG_TAG, "getBitmapFromSdoObservable: done!");
        });
        return bmpObservable;
    }

    public Bitmap getBitmapFromSdoSync(){
        String type = mSharedHelper.getString(mContext.getString(R.string.pref_image_type));
        String res = mSharedHelper.getString(mContext.getString(R.string.pref_image_resolution));

        Bitmap bmp = LoadServiceFactory.getLoadService().loadImageSync(type, res);

        OutputStream fout = null;
        File file = FileService.getOutputMediaFile(type);
        try {
            fout = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fout != null) {
            bmp.compress(Bitmap.CompressFormat.PNG, 95, fout);
            mSharedHelper.putString(mContext.getString(R.string.pref_stored_image), file.getAbsolutePath());
        }
        return bmp;
    }

    public Point getScreenSize() {
        //DisplayMetrics metrics = new DisplayMetrics();
        Point size = new Point();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        //windowManager.getDefaultDisplay().getMetrics(metrics);
        windowManager.getDefaultDisplay().getSize(size);
        return size;
    }

    public void setWallpaper(Bitmap bmp) throws IOException {
        WallpaperManager.getInstance(mContext).setBitmap(bmp);
    }

    public void showNotification(int result) {
        String text;
        switch (result) {
            case 1:
                text = mContext.getString(R.string.notification_body_ok);
                break;
            case 2:
                text = mContext.getString(R.string.notification_body_error);
                break;
            default:
                text = mContext.getString(R.string.notification_body_empty);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        notificationBuilder.setAutoCancel(false)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_clear)
                .setLargeIcon(BitmapService.largeIcon(mContext))
                .setContentTitle(mContext.getString(R.string.notification_title))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                //.setContentIntent(contentIntent(mContext))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(PENDING_INTENT_NOTIFICATION, notificationBuilder.build());

    }

    public void showErrorNotification(Throwable throwable) {
        String text;
        text = throwable.getMessage();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        notificationBuilder.setAutoCancel(false)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_clear)
                .setLargeIcon(BitmapService.largeIcon(mContext))
                .setContentTitle(mContext.getString(R.string.notification_title))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                //.setContentIntent(contentIntent(mContext))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(PENDING_INTENT_NOTIFICATION, notificationBuilder.build());
    }

    public String getStoredImageFilename() {
        return mSharedHelper.getString(mContext.getString(R.string.pref_stored_image));
    }

    public Observable<Bitmap> getBitmapFromFile(String filename) {
        return Observable.just(BitmapService.getBitmapFromFile(filename));
    }
}
