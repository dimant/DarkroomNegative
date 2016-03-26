package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;

/**
 * Created by diman on 3/26/2016.
 */
public interface IConvolutionMatrix {
    void setAll(double value);

    void setMatrix(double[][] config);

    Bitmap compute(Bitmap src);
}
