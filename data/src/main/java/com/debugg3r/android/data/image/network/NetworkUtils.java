package com.debugg3r.android.data.image.network;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class NetworkUtils {
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

    public static String getImageUrl(String type, String resolution) {
        String imageFormat = "latest_512_0171.jpg";

        if (type.equals("type_aia_193")) {
            imageFormat = "latest_512_0193.jpg";
        } else if (type.equals("type_aia_304")) {
            imageFormat = "latest_512_0304.jpg";
        } else if (type.equals("type_aia_171")) {
            imageFormat = "latest_512_0171.jpg";
        } else if (type.equals("type_aia_211")) {
            imageFormat = "latest_512_0211.jpg";
        } else if (type.equals("type_aia_131")) {
            imageFormat = "latest_512_0131.jpg";
        } else if (type.equals("type_aia_335")) {
            imageFormat = "latest_512_0335.jpg";
        } else if (type.equals("type_aia_094")) {
            imageFormat = "latest_512_0094.jpg";
        } else if (type.equals("type_aia_1600")) {
            imageFormat = "latest_512_1600.jpg";
        } else if (type.equals("type_aia_1700")) {
            imageFormat = "latest_512_1700.jpg";
        } else if (type.equals("type_aia_211_193_171")) {
            imageFormat = "latest_512_211193171.jpg";
        } else if (type.equals("type_aia_304_211_171")) {
            imageFormat = "f_304_211_171_512.jpg";
        } else if (type.equals("type_aia_094_335_193")) {
            imageFormat = "f_094_335_193_512.jpg";
        } else if (type.equals("type_aia_171_hmib")) {
            imageFormat = "f_HMImag_171_512.jpg";
        }
        imageFormat = imageFormat.replace("512", resolution);

        String imageUrl = "https://sdo.gsfc.nasa.gov/assets/img/latest/" + imageFormat;

        return imageUrl;
    }

}
