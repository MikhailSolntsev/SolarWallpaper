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

        showCurrentImage();
    }

    public void setmState(MainViewState mState) {
        this.mState = mState;
    }

    private boolean isViewAttached() {
        return mView == null;
    }

    @Override
    public void attachView(MainView view) {
        mView = view;
        mState.applyState(mView);
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void loadCurrentImage() {
        mState.setState(MainViewState.STATE_LOADING);
        mState.applyState(mView);

        mDataManager.getBitmapFromSdoObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //  onNext
                        bmp -> {
                            mState.setState(MainViewState.STATE_IMAGE);
                            mState.setBitmap(bmp);
                            mState.applyState(mView);
                        },
                        //  onError
                        throwable -> {mView.hideProgress();
                                throw new NullPointerException(throwable.getMessage());},
                        //  onComplete
                        () -> {
                            mState.setState(MainViewState.STATE_IMAGE);
                            mState.applyState(mView);
                        });
    }

    @Override
    public void showCurrentImage() {
        // 1. look for saved picture
        String filename = mDataManager.getStoredImageFilename();
        if (filename.isEmpty()) {
            loadCurrentImage();
        } else {
            // 2. if it is present, load it
            mState.setState(MainViewState.STATE_LOADING);

            mDataManager.getBitmapFromFile(filename)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    // 3. set image
                    .subscribe(bmp -> {
                        mState.setBitmap(bmp);
                        mState.setState(MainViewState.STATE_IMAGE);
                    });
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

}
