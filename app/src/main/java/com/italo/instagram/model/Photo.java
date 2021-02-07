package com.italo.instagram.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Photo implements Serializable {

    private String id;
    private byte[] imageBytes;
    private Bitmap imageBitmap;
    private Bitmap imageBitmapFilter;

    // ----------------------------------------------------------------------------------


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmapFilter() {
        return imageBitmapFilter;
    }

    public void setImageBitmapFilter(Bitmap imageBitmapFilter) {
        this.imageBitmapFilter = imageBitmapFilter;
    }
}
