package com.debugg3r.android.solarwallpaper.presenter;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import com.debugg3r.android.solarwallpaper.model.BitmapService;
import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.view.MainView;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenterImpl implements MainPresenter {

    private static final String LOG_TAG = "MAIN_PRESENTER";
    private DataManager mDataManager;
    private MainView mView;

    private MainView getView() {
        return mView;
    }

    public MainPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
        mView = null;
    }

    private boolean isViewAttached() {
        return mView == null;
    }

    @Override
    public void attachView(MainView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void loadCurrentImage() {
        mView.showProgress();
        mDataManager.getBitmapFromSdoObservable()
                .observeOn(Schedulers.computation())
                .flatMap(bmp -> {
                            int height = mView.getImageHeight();
                            int width = mView.getImageWidth();
                            bmp = BitmapService.fitBitmapToSize(bmp, height, width);
                            return Observable.just(bmp);
                        },
                        throwable -> Observable.create(f -> f.onError(throwable)),
                        () -> Observable.create(f -> f.onCompleted()))
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
                        mView.showToast("Can't set wallpaper due to error " + e.getMessage());
                    }

                    mDataManager.showNotification(1);
                });
    }
}
