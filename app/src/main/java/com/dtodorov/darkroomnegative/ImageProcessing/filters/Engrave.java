package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;

import com.dtodorov.darkroomnegative.ImageProcessing.ConvolutionMatrix;
import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class Engrave implements IFilter {
    @Override
    public Bitmap apply(Bitmap bitmap) {
        ConvolutionMatrix convolutionMatrix = new ConvolutionMatrix();
        convolutionMatrix.setAll(0);
        convolutionMatrix.Matrix[0][0] = -2;
        convolutionMatrix.Matrix[1][1] = 2;
        convolutionMatrix.Factor = 1;
        convolutionMatrix.Offset = 95;
        return convolutionMatrix.compute(bitmap);
    }
}
