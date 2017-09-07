package com.debugg3r.android.solarwallpaper.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.debugg3r.android.solarwallpaper.R;

public class BitmapService {
    static public Bitmap getBitmapFromResource(Context mContext, int drawable) {
        return BitmapFactory.decodeResource(mContext.getResources(), drawable);
    }

    public static Bitmap fitBitmapToSize(Bitmap bitmap, int height, int width) {

        return null;
    }
}
