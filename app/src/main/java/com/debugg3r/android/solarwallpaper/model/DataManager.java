package com.debugg3r.android.solarwallpaper.model;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.WindowManager;

import com.debugg3r.android.solarwallpaper.R;
import com.debugg3r.android.solarwallpaper.model.BitmapService;

import java.io.IOException;

public class DataManager {

    private final Context mContext;
    private SharedPreferencesHelper mSharedHelper;

    public DataManager(Context context) {
        this.mContext = context;

        mSharedHelper = new SharedPreferencesHelper(mContext);
    }

    public Bitmap getBitmapFromResource() {
        return BitmapService.getBitmapFromResource(mContext, R.drawable.latest);
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
}
