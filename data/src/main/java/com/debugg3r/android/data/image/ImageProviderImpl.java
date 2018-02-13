package com.debugg3r.android.data.image;

import com.debugg3r.android.data.image.network.SdoClient;
import com.debugg3r.android.domain.ImageContainer;
import com.debugg3r.android.domain.ImageProvider;

import io.reactivex.Single;

public class ImageProviderImpl implements ImageProvider {
    @Override
    public Single<ImageContainer> getNewImage(String type, String size) {
        return Single.defer(() ->{
            SdoClient client = new SdoClient();
            return client.getImage(type, size);
        });
    }

    @Override
    public Single<ImageContainer> getOldImage(String type, String size) {
        return Single.defer(() -> {

            return Single.just(new ImageContainer());
        });
    }
}
