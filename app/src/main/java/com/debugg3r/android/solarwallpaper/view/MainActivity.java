package com.debugg3r.android.solarwallpaper.view;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.debugg3r.android.solarwallpaper.R;
import com.debugg3r.android.solarwallpaper.SolarApplication;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    @Inject
    MainPresenter mainPresenter;

    private ImageView mImageViewWall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SolarApplication.getComponent().inject(this);

        Button buttonSet = (Button) findViewById(R.id.button_set_wallpaper);
        mImageViewWall = (ImageView) findViewById(R.id.image_view_wall);

        buttonSet.setOnClickListener(
                (view) -> {
                    mainPresenter.loadCurrentImage();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainPresenter.deattachView();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setImage(Bitmap image) {
        mImageViewWall.setImageBitmap(image);
    }

    @Override
    public int getImageHeight() {
        Log.d(LOG_TAG, "Image height: " + String.valueOf(mImageViewWall.getHeight()));
        return mImageViewWall.getHeight();
    }

    @Override
    public int getImageWidth() {
        Log.d(LOG_TAG, "Image width: " + String.valueOf(mImageViewWall.getWidth()));
        return mImageViewWall.getWidth();
    }
}
