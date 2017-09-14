package com.debugg3r.android.solarwallpaper.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.debugg3r.android.solarwallpaper.model.BitmapService;
import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.view.MainView;

public class MainPresenterImpl implements MainPresenter{

    private static final String LOG_TAG = "MAIN_PRESENTER";
    private DataManager mDataManager;
    private MainView mView;
    private MainView mDummyView;

    private MainView getView() {
        return mView;
    }

    public MainPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;

        mDummyView = getDummyView();
        mView = mDummyView;
    }

    private MainView getDummyView() {
        return new MainView() {
            @Override
            public void showProgress() {Log.d(LOG_TAG, "Dummy show progress");}

            @Override
            public void hideProgress() {Log.d(LOG_TAG, "Dummy hide progress");}

            @Override
            public void setImage(Bitmap image) {Log.d(LOG_TAG, "Dummy set image");}

            @Override
            public int getImageHeight() {Log.d(LOG_TAG, "Dummy get height"); return 0;}

            @Override
            public int getImageWidth() {Log.d(LOG_TAG, "Dummy get width"); return 0;}
        };
    }

    @Override
    public void attachView(MainView view) {
        mView = view;
    }

    @Override
    public void deattachView() {
        mView = mDummyView;
    }

    @Override
    public void loadCurrentImage() {
        Bitmap bmp = mDataManager.getBitmapFromResource();
        int height = mView.getImageHeight();
        int width = mView.getImageWidth();
        bmp = BitmapService.fitBitmapToSize(bmp, height, width);
        mView.setImage(bmp);
    }

    @Override
    public void scheduleJob() {

    }
}