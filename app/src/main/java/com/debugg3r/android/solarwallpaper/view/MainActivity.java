package com.debugg3r.android.solarwallpaper.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.debugg3r.android.solarwallpaper.R;
import com.debugg3r.android.solarwallpaper.SolarApplication;
import com.debugg3r.android.solarwallpaper.model.SharedPreferencesHelper;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final String LOG_TAG = "MAIN_ACTIVITY";

    @Inject
    MainPresenter mainPresenter;

    private ImageView mImageViewWall;
    private ProgressBar mProgressBar;
    private SharedPreferencesHelper mSharedHelper;
    private String mImageType;
    private Point mImageSize;

    private AlertDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SolarApplication.getComponent().inject(this);

        mImageViewWall = (ImageView) findViewById(R.id.image_view_wall);

        Button buttonShow = (Button) findViewById(R.id.button_show_image);
        buttonShow.setOnClickListener((view) -> mainPresenter.loadCurrentImage());

        Button buttonSet = (Button) findViewById(R.id.button_set_wallpaper);
        buttonSet.setOnClickListener((view) -> mainPresenter.setWallpaper());

        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);

        mSharedHelper = new SharedPreferencesHelper(this);
        mImageType = mSharedHelper.getString(getString(R.string.pref_image_type));

    }

    @Override
    protected void onResume() {
        super.onResume();

        mainPresenter.attachView(this);

        mainPresenter.showCurrentImage();

        // check for image type change in settings
        String newType = mSharedHelper.getString(getString(R.string.pref_image_type));
        if (!newType.equals(mImageType)) {
            mImageType = newType;
            mainPresenter.loadCurrentImage();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.menu_item_exit:
                finish();
                break;
        }
        return true;
    }


    @Override
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new AlertDialog.Builder(this)
                    .setView(new ProgressBar(this))
                    .create();
        }
        mProgressDialog.show();
//        mProgressBar.setVisibility(View.VISIBLE);
//        mImageViewWall.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
//        mProgressBar.setVisibility(View.GONE);
//        mImageViewWall.setVisibility(View.VISIBLE);
    }

    @Override
    public void setImage(Bitmap image) {
        mImageViewWall.setImageBitmap(image);
    }

    @Override
    public Point getImageSize() {
        mImageSize = new Point(mImageViewWall.getHeight(), mImageViewWall.getWidth());
        return mImageSize;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }
}
