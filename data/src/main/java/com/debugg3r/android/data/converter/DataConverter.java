package com.debugg3r.android.data.converter;

import android.graphics.Bitmap;

import com.debugg3r.android.domain.ImageContainer;

import io.reactivex.Single;

public interface DataConverter {
    Single<Bitmap> convertToBitmap(ImageContainer container);
}
