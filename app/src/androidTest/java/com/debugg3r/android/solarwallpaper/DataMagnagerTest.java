package com.debugg3r.android.solarwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.model.FileService;
import com.debugg3r.android.solarwallpaper.model.SharedPreferencesHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class DataMagnagerTest {

    private static final String LOG_TAG = "DATA_MNG_TEST";
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
        Observable<Bitmap> obs = mDataManager.getBitmapFromSdoObservable();
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
//        Observable<Bitmap> obs = mDataManager.getBitmapFromSdoObservable();
//        assertNotNull(obs);
//        Bitmap bmp = obs.toBlocking().first();
//        try {
//            mDataManager.setWallpaper(bmp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void getFileName() {
        String type = "AIA312";
        OutputStream fout;
        File file = FileService.getOutputMediaFile(type);
        assertNotNull("File is null", file);
        try {
            fout = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Exception", e);
            assertTrue("Ex", false);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Exception", e);
            assertTrue("Ex", false);
        }
    }
}

