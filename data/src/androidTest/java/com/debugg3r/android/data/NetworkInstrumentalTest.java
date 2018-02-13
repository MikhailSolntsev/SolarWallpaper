package com.debugg3r.android.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.debugg3r.android.data.image.network.MySslSocketFactory;
import com.debugg3r.android.data.image.network.NetworkUtils;
import com.debugg3r.android.data.image.network.SdoClient;
import com.debugg3r.android.domain.ImageContainer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class NetworkInstrumentalTest {
    private Context mContext;
    private String mType = "type_aia_171";
    private String mSize = "1024";

    @Before
    public void beforeTest() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void useAppContext() throws Exception {

    }

    @Test
    public void getImageTestOk() {

        // init
        AtomicInteger flag = new AtomicInteger(1);

        SdoClient client = new SdoClient();
        client.getImage(mType, mSize)
                //.subscribeOn(Schedulers.io())
                .subscribe(
                        container -> {
                            synchronized (flag) {
                                flag.set(2);
                                flag.notify();
                            }
                        },
                        throwable -> {
                            Log.d("TEST_TAG", throwable.getMessage());
                            synchronized (flag) {
                                flag.set(3);
                                flag.notify();
                            }
                        });


//        synchronized (flag) {
//            try {
//                flag.wait();
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//                assertTrue("Error due to wait", false);
//            }
//        }

        assertEquals("Flag is not equal 2", 2, flag.get());
    }

    @Test
    public void urlCorrectTest() {
        String url = NetworkUtils.getImageUrl(mType, mSize);
        assertTrue("URL string is empty", !url.isEmpty());

        assertEquals("Url isn't correct!",
                "https://sdo.gsfc.nasa.gov/assets/img/latest/latest_1024_0171.jpg",
                url);
    }

    @Test
    public void connectionTest() {
        URL url;
        try {
            url = new URL(NetworkUtils.getImageUrl(mType, mSize));
        } catch (MalformedURLException ex) {
            assertTrue("Can't create URL", false);
            return;
        }

        // get connection
        HttpsURLConnection urlConnection;
        try {
            // get request
            urlConnection = (HttpsURLConnection) url.openConnection();
        } catch (IOException ex) {
            assertTrue("Can't get url connection", false);
            return;
        }

        urlConnection.setSSLSocketFactory(new MySslSocketFactory(urlConnection.getSSLSocketFactory()));

        // get response
        InputStream inputStream;
        try {
            // get data from response
            inputStream = urlConnection.getInputStream();
        } catch (IOException ex) {
            assertTrue("Can't get input stream", false);
            return;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        try {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException ex) {
            assertTrue("Can't copy from input stream to byte array", false);
            return;
        }

        ImageContainer container = new ImageContainer(buffer.toByteArray(), mType, mSize);
    }
}
