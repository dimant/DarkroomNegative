package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;

import com.dtodorov.darkroomnegative.ImageProcessing.ConvolutionMatrix;
import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class Sharpen implements IFilter {
    private double _weight;

    public void configure(double weight) {
        _weight = weight;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        double[][] SharpConfig = new double[][] { { 0, -2, 0 },
                { -2, _weight, -2 }, { 0, -2, 0 } };
        ConvolutionMatrix convolutionMatrix = new ConvolutionMatrix();
        convolutionMatrix.setMatrix(SharpConfig);
        convolutionMatrix.Factor = _weight - 8;
        return convolutionMatrix.compute(bitmap);
    }
}
