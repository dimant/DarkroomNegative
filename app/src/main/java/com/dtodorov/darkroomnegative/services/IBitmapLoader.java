package com.dtodorov.darkroomnegative.services;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.FileNotFoundException;

/**
 * Created by ditodoro on 3/25/2016.
 */
public interface IBitmapLoader {
    Bitmap Load(Uri uri) throws FileNotFoundException;
}
