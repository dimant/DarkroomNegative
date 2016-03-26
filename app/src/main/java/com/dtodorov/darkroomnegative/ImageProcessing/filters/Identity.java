package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class Identity implements IFilter {
    @Override
    public Bitmap apply(Bitmap bitmap) {
        return bitmap;
    }
}
