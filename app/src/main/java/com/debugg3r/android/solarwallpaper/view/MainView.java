package com.debugg3r.android.solarwallpaper.view;

import android.graphics.Bitmap;
import android.graphics.Point;

public interface MainView {
    void showProgress();

    void hideProgress();

    void setImage(Bitmap image);

    Point getImageSize();

    void showToast(String s);
}
