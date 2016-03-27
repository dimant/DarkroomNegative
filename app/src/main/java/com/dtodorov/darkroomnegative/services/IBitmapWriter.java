package com.dtodorov.darkroomnegative.services;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by diman on 3/26/2016.
 */
public interface IBitmapWriter {
    void Write(Bitmap bitmap, String filename) throws IOException;
}
