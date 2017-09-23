package com.debugg3r.android.solarwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import com.debugg3r.android.solarwallpaper.model.BitmapService;
import com.debugg3r.android.solarwallpaper.model.DataManager;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenter;
import com.debugg3r.android.solarwallpaper.presenter.MainPresenterImpl;
import com.debugg3r.android.solarwallpaper.view.MainView;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import rx.Observable;

@RunWith(AndroidJUnit4.class)
public class MainPresenterTest {

    private Context mContext;
    private Bitmap mBitmap;
    private DataManager mDataManager;
    private MainView mMainView;
    private MainPresenter mMainPresenter;

    private String type = "type_aia_094";
    private String resolution = "256";

    @Before
    public void setupDummy() {
        setupContext();
        setupBitmap();
        setupDataManager();
        setupMainView();

        mMainPresenter = new MainPresenterImpl(mDataManager);
        mMainPresenter.attachView(mMainView);
    }

    private void setupBitmap() {
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.latest);
    }

    private void setupContext() {
//        mContext = new MockContext();
        mContext = InstrumentationRegistry.getTargetContext();
    }

    private void setupMainView() {
        mMainView = new MainView() {
            @Override
            public void showProgress() {}

            @Override
            public void hideProgress() {}

            @Override
            public void setImage(Bitmap image) {
                assertNotNull("Image to set is null", image);
                assertTrue("Image to set is empty", image.getHeight() > 0);
            }

            @Override
            public int getImageHeight() {
                return 64;
            }

            @Override
            public int getImageWidth() {
                return 64;
            }

            @Override
            public void showToast(String s) {
                System.out.println(s);
            }
        };
    }

    private void setupDataManager() {
        mDataManager = new DataManager(mContext) {
            @Override
            public Point getScreenSize() {
                Point result = new Point(102, 76);
                return result;
            }

            @Override
            public void setWallpaper(Bitmap bmp) throws IOException {
                assertNotNull("Wallpaper bmp is null", bmp);
                assertTrue("Wallpaper bmp is empty", bmp.getWidth() > 0);
            }

            @Override
            public Bitmap getBitmapFromResource() {
                return mBitmap;
            }

            @Override
            public Observable<Bitmap> getBitmapFromSdo() {
                return Observable.just(mBitmap);
            }
        };
    }

    @Test
    public void testGetImage() throws Exception {
        mMainPresenter.loadCurrentImage();
    }

    @Test
    public void testSetWallpaper()  throws Exception {
        mMainPresenter.setWallpaper();
    }
}
