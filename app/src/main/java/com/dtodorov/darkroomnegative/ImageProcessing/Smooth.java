package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;

/**
 * Created by diman on 3/26/2016.
 */
public class Smooth implements IFilter {
    private double _value;

    public void configure(double value) {
        _value = value;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        ConvolutionMatrix convolutionMatrix = new ConvolutionMatrix();
        convolutionMatrix.setAll(1);
        convolutionMatrix.Matrix[1][1] = _value;
        convolutionMatrix.Factor = _value + 8;
        convolutionMatrix.Offset = 1;
        return convolutionMatrix.compute(bitmap);
    }
}
