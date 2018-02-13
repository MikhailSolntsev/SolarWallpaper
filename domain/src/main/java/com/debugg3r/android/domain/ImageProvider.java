package com.debugg3r.android.domain;

import io.reactivex.Single;

public interface ImageProvider {
    Single<ImageContainer> getNewImage(String type, String size);

    Single<ImageContainer> getOldImage(String type, String size);
}
