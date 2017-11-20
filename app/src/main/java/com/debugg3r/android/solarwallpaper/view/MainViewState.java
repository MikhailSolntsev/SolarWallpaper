package com.debugg3r.android.solarwallpaper.view;

import android.graphics.Bitmap;

public class MainViewState {
    public final static int STATE_NOTHING = 1;
    public final static int STATE_LOADING = 2;
    public final static int STATE_IMAGE = 3;

    private int mState = STATE_NOTHING;
    private Bitmap mBitmap = null;

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}
