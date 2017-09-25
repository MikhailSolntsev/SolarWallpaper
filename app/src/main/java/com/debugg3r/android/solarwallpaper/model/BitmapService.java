package com.debugg3r.android.solarwallpaper.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.debugg3r.android.solarwallpaper.R;

import java.io.InputStream;

public class BitmapService {
    private static final String LOG_TAG = "BITMAP_SERVICE";

    static public Bitmap getBitmapFromResource(Context mContext, int drawable) {
        return BitmapFactory.decodeResource(mContext.getResources(), drawable);
    }

    static public Bitmap getBitmapFromStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap fitBitmapToSize(Bitmap bitmap, int height, int width) {
        int originHeight = bitmap.getHeight();
        Log.d(LOG_TAG, "Original height: " + String.valueOf(originHeight));
        int originWidth = bitmap.getWidth();
        Log.d(LOG_TAG, "Original width: " + String.valueOf(originWidth));
        float scaleX = (float)height / originHeight;
        Log.d(LOG_TAG, "Original scale X: " + String.valueOf(scaleX));
        float scaleY = (float)width / originWidth;
        Log.d(LOG_TAG, "Original scale Y: " + String.valueOf(scaleY));
        float scale = Math.max(scaleX, scaleY);
        Log.d(LOG_TAG, "Original scale: " + String.valueOf(scale));
        return Bitmap.createScaledBitmap(bitmap, (int)(originHeight * scale), (int)(originWidth * scale), false);

    }

    public static Bitmap largeIcon(Context context) {
        Resources resources = context.getResources();
        return BitmapFactory.decodeResource(resources, R.drawable.ic_info_black_24dp);
    }
}
