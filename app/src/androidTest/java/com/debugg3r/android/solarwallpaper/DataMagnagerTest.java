package com.debugg3r.android.solarwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.model.SharedPreferencesHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class DataMagnagerTest {

    private DataManager mDataManager;
    private SharedPreferencesHelper mSharedHelper;
    private Context mContext;

    @Before
    public void setupData() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        mDataManager = new DataManager(appContext);
        mSharedHelper = new SharedPreferencesHelper(appContext);
        mContext = appContext;
    }

    @Test
    public void getBitmapFromSdo_isCorrect() {
        Observable<Bitmap> obs = mDataManager.getBitmapFromSdo();
        assertNotNull(obs);
        Bitmap bmp = obs.toBlocking().first();
        assertNotNull("Bitmap is null", bmp);
    }

    @Test
    public void getScreenSize_IsCorrect() {
        Point screen = mDataManager.getScreenSize();
        assertTrue("X = 0", screen.x > 0);
        assertTrue("Y = 0", screen.y > 0);
    }

    @Test
    public void setWallpaper_isCorrect() {
//        Observable<Bitmap> obs = mDataManager.getBitmapFromSdo();
//        assertNotNull(obs);
//        Bitmap bmp = obs.toBlocking().first();
//        try {
//            mDataManager.setWallpaper(bmp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
