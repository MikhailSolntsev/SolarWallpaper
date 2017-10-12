package com.debugg3r.android.solarwallpaper.model.loadservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import rx.Observable;
import rx.schedulers.Schedulers;

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

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Observable<Bitmap> loadImageObservable(String type, String res) {
        String imageUrl = LoadService.getImageUrl(type, res);
        return Observable.just(imageUrl)
                .observeOn(Schedulers.io())
                .flatMap(url -> {
                    OkHttpClient client = getUnsafeOkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    return Observable.just(client.newCall(request));
                })
                // do call
                .flatMap(call -> {
                    try {
                        return Observable.just(call.execute());
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                })
                // get response
                .flatMap(response -> {
                            try{
                                return Observable.just(response.body().bytes());
                            } catch (IOException e) {
                                return Observable.create(f -> f.onError(e));
                            }
                        },
                        throwable -> Observable.create(f -> f.onError(throwable)),
                        () -> Observable.create(f -> f.onCompleted()))
                // load bitmap
                .flatMap(bytes -> Observable.just(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)),
                        throwable -> Observable.create(f -> f.onError(throwable)),
                        () -> Observable.create(f -> f.onCompleted())
                );

    }

    @Override
    public Bitmap loadImageSync(String type, String res) {
        return null;
    }
}
