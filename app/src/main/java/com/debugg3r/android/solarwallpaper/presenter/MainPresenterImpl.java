package com.debugg3r.android.solarwallpaper.presenter;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import com.debugg3r.android.solarwallpaper.model.BitmapService;
import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.view.MainView;
import com.debugg3r.android.solarwallpaper.view.MainViewState;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenterImpl implements MainPresenter {

    private static final String LOG_TAG = "MAIN_PRESENTER";
    private DataManager mDataManager;
    private MainView mView;

    private MainViewState mState;

    public MainPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
        mView = null;
        mState = new MainViewState();
        mState.setState(MainViewState.STATE_NOTHING);
        // todo check for loaded image, cache it
    }

    private boolean isViewAttached() {
        return mView == null;
    }

    @Override
    public void attachView(MainView view) {
        mView = view;
        if (isViewAttached())
            mView.applyState(mState);
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void loadCurrentImage() {
        mState.setState(MainViewState.STATE_LOADING);
        if (isViewAttached()) {
            mView.applyState(mState);
        }

        mDataManager.getBitmapFromSdoObservable()
                .observeOn(Schedulers.computation())
                .map(bmp -> {
                    Point point;
                    if (isViewAttached()) {
                        point = mView.getImageSize();
                    } else {
                        point = new Point(1280, 720);
                    }
                    bmp = BitmapService.fitBitmapToSize(bmp, point.x, point.y);
                    return bmp;})
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bmp -> {
                            mView.hideProgress();
                            mView.setImage(bmp);
                        },
                        throwable -> {mView.hideProgress();
                                throw new NullPointerException(throwable.getMessage());},
                        () -> mView.hideProgress());
    }

    @Override
    public void showCurrentImage() {
        // 1. look for saved picture
        String filename = mDataManager.getStoredImageFilename();
        if (filename.isEmpty()) {
            loadCurrentImage();
        } else {
            // 2. if it is present, load it
            mDataManager.getBitmapFromFile(filename)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    // 3. set image
                    .subscribe(bmp -> mView.setImage(bmp));
        }
    }

    @Override
    public void setWallpaper() {
        mDataManager.getBitmapFromSdoObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(bmp -> {
                    Point size = mDataManager.getScreenSize();
                    bmp = BitmapService.fitBitmapToSize(bmp, size.y, size.x);
                    return bmp;
                })
                .subscribe(bmp -> {
                    try {
                        mDataManager.setWallpaper(bmp);

                    } catch (IOException e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            mView.showToast("Can't set wallpaper due to error " + e.getMessage());
                        }
                    }

                    mDataManager.showNotification(1);
                });
    }

    @Override
    public String checkLoadedType(String imageType) {
        String newType = "";
        return newType;
    }

}
