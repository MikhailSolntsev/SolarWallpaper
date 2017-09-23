package com.debugg3r.android.solarwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.debugg3r.android.solarwallpaper.model.loadservice.LoadService;
import com.debugg3r.android.solarwallpaper.model.loadservice.LoadServiceJelly;
import com.debugg3r.android.solarwallpaper.model.loadservice.MySslSocketFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class LoadServiceJellyTest {

    private Context mContext;
    private LoadService mLoadService;
    private String mUrl;
    private String type = "type_aia_171";
    private String resolution = "512";

    @Before
    public void setupEnvironment() {
        mContext = InstrumentationRegistry.getTargetContext();
        mLoadService = LoadServiceJelly.getInstance();
        mUrl = LoadService.getImageUrl(type, resolution);
    }

    @Test
    public void getHttpsConnection() throws Exception {
        URL url = new URL(mUrl);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(new MySslSocketFactory(urlConnection.getSSLSocketFactory()));
        InputStream in = urlConnection.getInputStream();
        Bitmap bmp = BitmapFactory.decodeStream(in);
        assertNotNull("bmp is null", bmp);
    }

    @Test
    public void getBitmap() {
        Observable<Bitmap> observable = LoadServiceJelly.getInstance().loadImage(type, resolution);
        Bitmap bmp = observable.toBlocking().first();
        assertNotNull("Bitmap is null", bmp);
    }
}
