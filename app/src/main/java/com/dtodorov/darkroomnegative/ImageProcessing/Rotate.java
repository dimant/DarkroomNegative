package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by diman on 3/26/2016.
 */
public class Rotate implements IFilter {
    private float _degree;

    public void configure(float degree) {
        _degree = degree;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(_degree);

        // return new bitmap rotated using matrix
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
