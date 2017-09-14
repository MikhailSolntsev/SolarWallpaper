package com.debugg3r.android.solarwallpaper.view;

import android.graphics.Bitmap;

public interface MainView {
    void showProgress();

    void hideProgress();

    void setImage(Bitmap image);

    int getImageHeight();

    int getImageWidth();
}
