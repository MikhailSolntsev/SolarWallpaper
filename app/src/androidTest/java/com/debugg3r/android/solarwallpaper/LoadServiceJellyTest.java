package com.debugg3r.android.solarwallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.model.loadservice.LoadService;
import com.debugg3r.android.solarwallpaper.model.loadservice.LoadServiceFactory;
import com.debugg3r.android.solarwallpaper.model.loadservice.LoadServiceJelly;
import com.debugg3r.android.data.image.network.MySslSocketFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class LoadServiceJellyTest {

    private static final String LOG_TAG = "SOLAR_TEST";
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
        Observable<Bitmap> observable = LoadServiceJelly.getInstance().loadImageObservable(type, resolution);
        Bitmap bmp = observable.toBlocking().first();
        assertNotNull("Bitmap is null", bmp);
    }

    @Test
    public void getAsyncBitmap() {
        final CountDownLatch signal = new CountDownLatch(1);

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String[] params) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String type = sharedPref.getString(mContext.getString(R.string.pref_image_type), "");
                String res = sharedPref.getString(mContext.getString(R.string.pref_image_resolution), "");
                Bitmap bmp = LoadServiceFactory.getLoadService().loadImageSync(type, res);

                Log.d(LOG_TAG, "bmp load is completed");

                assertNotNull("Bmp is null", bmp);

                Log.d(LOG_TAG, "bmp is not null, who-ho!");
                signal.countDown();

                return "Ok";
            }
        };

        task.executeOnExecutor(r -> r.run(), "");
        //task.execute();

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Test completed! Who-ho");
        Log.d("SOLAR_TEST", "Test completed! Who-ho");
    }

    @Test
    public void getSyncBitmap() {
        assertNotNull("Context is null", mContext);

        DataManager dataManager = new DataManager(mContext);
        assertNotNull("DataManager is null", dataManager);

        Point size = dataManager.getScreenSize();
        assertNotEquals("Screen size iz 0", 0, size.x);
        assertNotEquals("Screen size iz 0", 0, size.y);

        Bitmap bmp = dataManager.getBitmapFromSdoSync();
        assertNotNull("Bmp is null", bmp);
    }

    @Test
    public void getSingle() {
        LoadService service = LoadServiceFactory.getLoadService();
    }

}
