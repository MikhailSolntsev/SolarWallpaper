package com.debugg3r.android.domain;

public class ImageContainer {
    private byte[] mImage;
    private String mType;
    private String mSize;

    public ImageContainer(){
        this.mImage = new byte[0];
        mSize = "";
        mType = "";
    }

    public ImageContainer(byte[] mImage, String type, String size) {
        this.mImage = mImage;
        mSize = size;
        mType = type;
    }

    public byte[] getImage() {
        return mImage;
    }

    public String getSize() {
        return mSize;
    }

    public String getType() {
        return mType;
    }
}
