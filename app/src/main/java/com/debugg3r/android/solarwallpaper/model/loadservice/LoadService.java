package com.debugg3r.android.solarwallpaper.model.loadservice;

import android.graphics.Bitmap;

import rx.Observable;
import rx.Single;

public abstract class LoadService {

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

    public abstract Observable<Bitmap> loadImageObservable(String type, String res);

    public abstract Single<byte[]> loadImageSinge(String type, String res);

    public abstract Bitmap loadImageSync(String type, String res);
}
