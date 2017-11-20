package com.debugg3r.android.solarwallpaper.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.debugg3r.android.solarwallpaper.R;
import com.debugg3r.android.solarwallpaper.SolarApplication;
import com.debugg3r.android.solarwallpaper.model.SharedPreferencesHelper;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final String LOG_TAG = "MAIN_ACTIVITY";

    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.image_view_wall)
    ImageView mImageViewWall;

    private AlertDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        SolarApplication.getComponent().inject(this);

}

    @OnClick(R.id.button_show_image)
    public void onClickShowImage(View view) {
        mainPresenter.loadCurrentImage();
    }

    @OnClick(R.id.button_set_wallpaper)
    public void onClickSetWallpaper(View view) {
        mainPresenter.setWallpaper();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mainPresenter.attachView(this);
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
            ProgressBar bar = new ProgressBar(this);
            ViewGroup.LayoutParams params = bar.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;

            //bar.setLayoutParams(new ActionBar.LayoutParams());
            mProgressDialog = new AlertDialog.Builder(this)
                    .setView(bar)
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
        if (image != null)
            mImageViewWall.setImageBitmap(image);
    }

    @Override
    public Point getImageSize() {
        Point mImageSize = new Point(mImageViewWall.getHeight(), mImageViewWall.getWidth());
        return mImageSize;
    }

    @Override
    public void showToast(String message) {
        Observable.defer(() -> Observable.just(message))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> Toast.makeText(this, text, Toast.LENGTH_SHORT));
    }

    @Override
    public void applyState(MainViewState state) {
        switch (state.getState()){
            case MainViewState.STATE_IMAGE:
                hideProgress();
                setImage(state.getBitmap());
                break;
            case MainViewState.STATE_LOADING:
                showProgress();
                break;
            default:
                hideProgress();
        }
    }

}
