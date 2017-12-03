package com.debugg3r.android.solarwallpaper;

import android.support.test.runner.AndroidJUnit4;

import com.debugg3r.android.solarwallpaper.view.MainViewState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainViewStateTest {

    @Before
    public void setupTest(){}

    @Test
    public void testMainViewSetNullBitmap() {
        MainViewState viewState = new MainViewState();
        try {
            viewState.setBitmap(null);
        } catch (Exception ex) {
            assertNotNull("view state set null bitmap error");
        }
    }

    @Test
    public void testMainViewSetNullViewState() {
        MainViewState viewState = new MainViewState();
        try {
            viewState.setBitmap(null);
            viewState.setState(MainViewState.STATE_LOADING);
            viewState.applyState(null);

            viewState.setState(MainViewState.STATE_IMAGE);
            viewState.applyState(null);
        } catch (Exception ex) {
            assertNotNull("view state set null bitmap error");
        }
    }
}
