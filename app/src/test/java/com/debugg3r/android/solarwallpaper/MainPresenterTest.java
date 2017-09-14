package com.debugg3r.android.solarwallpaper;

import android.content.Context;
import android.graphics.Bitmap;

import com.debugg3r.android.solarwallpaper.model.BitmapService;
import static org.junit.Assert.*;

import org.junit.Test;

public class MainPresenterTest {
    @Test
    public void testGetImage() {
        Context context = SolarApplication.getInstance().getApplicationContext();
        assertNotNull("App context is null", context);
        Bitmap bmp = BitmapService.getBitmapFromResource(context, R.drawable.latest);
        assertNotNull("BMP is null", bmp);
    }
}
