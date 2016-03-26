package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;

import com.dtodorov.darkroomnegative.ImageProcessing.ConvolutionMatrix;
import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class GaussianBlur implements IFilter {
    @Override
    public Bitmap apply(Bitmap bitmap) {
        double[][] GaussianBlurConfig = new double[][] { { 1, 2, 1 },
                { 2, 4, 2 }, { 1, 2, 1 } };
        ConvolutionMatrix convolutionMatrix = new ConvolutionMatrix();
        convolutionMatrix.setMatrix(GaussianBlurConfig);
        convolutionMatrix.Factor = 16;
        convolutionMatrix.Offset = 0;
        return convolutionMatrix.compute(bitmap);
    }
}
