package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class Flip implements IFilter {
    public enum Orientation {
        Vertical,
        Horizontal
    }

    private Orientation _type;

    public void configure(Orientation type) {
        _type = type;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (_type == Orientation.Vertical) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if (_type == Orientation.Horizontal) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true);
    }
}
