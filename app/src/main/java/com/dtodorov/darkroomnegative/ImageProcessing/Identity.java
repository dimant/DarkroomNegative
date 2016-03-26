package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;

/**
 * Created by diman on 3/26/2016.
 */
public class Identity implements IFilter {
    @Override
    public Bitmap apply(Bitmap bitmap) {
        return bitmap;
    }
}
