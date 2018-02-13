package com.debugg3r.android.solarwallpaper.model.loadservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static com.debugg3r.android.data.image.network.NetworkUtils.getUnsafeOkHttpClient;

public class LoadServiceKit extends LoadService {
    private static LoadServiceKit mInstance;
    private LoadServiceKit() {
    }

    public static LoadService getInstance() {
        if (mInstance == null) {
            mInstance = new LoadServiceKit();
        }
        return mInstance;
    }



    @Override
    public Observable<Bitmap> loadImageObservable(String type, String resolution) {
        final BehaviorSubject<Bitmap> result = BehaviorSubject.create();

        String imageUrl = LoadService.getImageUrl(type, resolution);
        Observable.just(imageUrl)
                .observeOn(Schedulers.io())
                .map(url -> {
                    OkHttpClient client = getUnsafeOkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Call call = client.newCall(request);

                    Response response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        result.onError(e);
                        //return Observable.error(e);
                        return null;
                    }

                    if (response == null) {
                        result.onCompleted();
                        return null;
                    }

                    if (response.body() != null) {
                        try {
                            byte[] body = response.body().bytes();
                            if (body == null) {
                                result.onCompleted();
                            }

                            return body;
                        } catch (IOException e) {
                            result.onError(e);
                        }
                    } else {
                        result.onCompleted();
                    }
                    return null;
                })
                .filter(body -> body != null)

                // load bitmap
                .subscribe(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    if (bmp == null) {
                        result.onCompleted();
                    }
                    result.onNext(bmp);
                });

        return result;
    }

    @Override
    public Single<byte[]> loadImageSinge(String type, String resolution) {
        return null;
    }


    @Override
    public Bitmap loadImageSync(String type, String res) {
        return null;
    }
}
