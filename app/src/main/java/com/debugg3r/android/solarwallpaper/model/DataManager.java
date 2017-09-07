package com.debugg3r.android.solarwallpaper.model;

import android.graphics.Bitmap;

import com.debugg3r.android.solarwallpaper.model.BitmapService;

public class DataManager {

    static public Bitmap getBitmapFromResource(int drawable) {
        return BitmapService.getBitmapFromResource(drawable);
    }
}
