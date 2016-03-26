package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;

/**
 * Created by diman on 3/26/2016.
 */
public class Emboss implements IFilter {
    @Override
    public Bitmap apply(Bitmap bitmap) {
        double[][] EmbossConfig = new double[][] { { -1, 0, -1 }, { 0, 4, 0 },
                { -1, 0, -1 } };
        ConvolutionMatrix convolutionMatrix = new ConvolutionMatrix();
        convolutionMatrix.setMatrix(EmbossConfig);
        convolutionMatrix.Factor = 1;
        convolutionMatrix.Offset = 127;
        return convolutionMatrix.compute(bitmap);
    }
}
