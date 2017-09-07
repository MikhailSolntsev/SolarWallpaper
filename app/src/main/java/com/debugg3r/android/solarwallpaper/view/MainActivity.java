package com.debugg3r.android.solarwallpaper.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.debugg3r.android.solarwallpaper.R;
import com.debugg3r.android.solarwallpaper.SolarApplication;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

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
                    mainPresenter.setBitmapToImageViewFromResource(mImageViewWall, R.drawable.latest);
                });
//        new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mImageViewWall.setImageResource(R.drawable.latest);
//                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.latest);
//
//                try {
//                    WallpaperManager.getInstance(MainActivity.this)
//                            .setBitmap(image);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
