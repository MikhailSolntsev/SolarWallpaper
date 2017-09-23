package com.debugg3r.android.solarwallpaper.model.loadservice;

public class LoadServiceFactory {
    public static LoadService getLoadService() {
        return LoadServiceJelly.getInstance();

    }
}
