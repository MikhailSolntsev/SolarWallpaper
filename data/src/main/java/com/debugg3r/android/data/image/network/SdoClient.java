package com.debugg3r.android.data.image.network;

import com.debugg3r.android.domain.ImageContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SdoClient {

    public Single<ImageContainer> getImage(String type, String size) {
        return Single.defer(() -> {
            URL url;
            try {
                url = new URL(NetworkUtils.getImageUrl(type, size));
            } catch (MalformedURLException ex) {
                return Single.error(ex);
            }

            // get connection
            HttpsURLConnection urlConnection;
            try {
                // get request
                urlConnection = (HttpsURLConnection) url.openConnection();
            } catch (IOException ex) {
                return Single.error(ex);
            }

            urlConnection.setSSLSocketFactory(new MySslSocketFactory(urlConnection.getSSLSocketFactory()));

            // get response
            InputStream inputStream;
            try {
                // get data from response
                inputStream = urlConnection.getInputStream();
            } catch (IOException ex) {
                return Single.error(ex);
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
                return Single.error(ex);
            }
            ImageContainer container = new ImageContainer(buffer.toByteArray(), type, size);
            return Single.just(container);});
    }

    public Single<ImageContainer> getImageOk(String type, String size) {
        OkHttpClient client = NetworkUtils.getUnsafeOkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.getImageUrl(type, size))
                .build();
        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException ex) {
            return Single.error(ex);
        }

        if (response == null) {
            return Single.just(new ImageContainer());
        }

        if (response.body() == null) {
            return Single.just(new ImageContainer());
        }

        byte[] body;
        try {
            body = response.body().bytes();
        } catch (IOException ex) {
            return Single.error(ex);
        }

        if (body == null) {
            return Single.just(new ImageContainer());
        }

        return Single.just(new ImageContainer(body, type, size));
    }
}
