package com.debugg3r.android.solarwallpaper.model.loadservice;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

public class LoadServiceJelly extends LoadService {

    private static LoadServiceJelly mInstance;

    private LoadServiceJelly() {
    }

    public static LoadService getInstance() {
        if (mInstance == null) {
            mInstance = new LoadServiceJelly();
        }
        return mInstance;
    }

    @Override
    public Observable<Bitmap> loadImageObservable(String type, String res) {
        String imageUrl = getImageUrl(type, res);

        Observable<Bitmap> result = Observable.just(imageUrl)
                .observeOn(Schedulers.io())
                .flatMap(urlString -> {

                    // get url
                    URL url;
                    try {
                        url = new URL(urlString);
                    } catch (MalformedURLException ex) {
                        return Observable.create(subscriber -> subscriber.onError(ex));
                    }

                    // get connection
                    HttpsURLConnection urlConnection;
                    try {
                        // get request
                        urlConnection = (HttpsURLConnection) url.openConnection();
                    } catch (IOException ex) {
                        return Observable.create(subscriber -> subscriber.onError(ex));
                    }
                    urlConnection.setSSLSocketFactory(new MySslSocketFactory(urlConnection.getSSLSocketFactory()));
                    return Observable.just(urlConnection);
                })
                .flatMap(urlConnection -> {

                            // get response
                            InputStream in;
                            try {
                                // get data from response
                                in = urlConnection.getInputStream();
                            } catch (IOException ex) {
                                return Observable.create(subscriber -> subscriber.onError(ex));
                            }

                            // create bitmap
                            Bitmap bmp = BitmapFactory.decodeStream(in);
                            return Observable.just(bmp);
                        }, throwable -> Observable.create(subscriber -> subscriber.onError(throwable))
                        , () -> Observable.create(subscriber -> subscriber.onCompleted()));
        return result;
    }

    @Override
    public Single<byte[]> loadImageSinge(String type, String res) {
        return null;
    }

    @Override
    public Bitmap loadImageSync(String type, String res) {

        String urlString = getImageUrl(type, res);

        // get url
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            return null;//Observable.create(subscriber -> subscriber.onError(ex));
        }

        // get connection
        HttpsURLConnection urlConnection;
        try {
            // get request
            urlConnection = (HttpsURLConnection) url.openConnection();
        } catch (IOException ex) {
            return null;//Observable.create(subscriber -> subscriber.onError(ex));
        }
        urlConnection.setSSLSocketFactory(new MySslSocketFactory(urlConnection.getSSLSocketFactory()));

        // get response
        InputStream in;
        try {
            // get data from response
            in = urlConnection.getInputStream();
        } catch (IOException ex) {
            return null;//Observable.create(subscriber -> subscriber.onError(ex));
        }

        // create bitmap
        Bitmap bmp = BitmapFactory.decodeStream(in);

        return bmp;
    }

}
