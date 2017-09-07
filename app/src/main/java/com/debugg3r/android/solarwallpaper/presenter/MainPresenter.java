package com.debugg3r.android.solarwallpaper.presenter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.debugg3r.android.solarwallpaper.model.BitmapService;
import com.debugg3r.android.solarwallpaper.model.DataManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainPresenter {

    private DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void setBitmapToImageViewFromResource(ImageView imageView, int drawable) {
        Bitmap bitmap = mDataManager.getBitmapFromResource(drawable);
//        int height = imageView.getHeight();
//        int width = imageView.getWidth();
//        bitmap = BitmapService.fitBitmapToSize(bitmap, height, width);
        imageView.setImageBitmap(bitmap);
    }

}
