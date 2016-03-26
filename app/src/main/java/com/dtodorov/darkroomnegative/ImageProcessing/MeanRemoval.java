package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;

/**
 * Created by diman on 3/26/2016.
 */
public class MeanRemoval implements IFilter {
    @Override
    public Bitmap apply(Bitmap bitmap) {
        double[][] MeanRemovalConfig = new double[][] { { -1, -1, -1 },
                { -1, 9, -1 }, { -1, -1, -1 } };
        ConvolutionMatrix convolutionMatrix = new ConvolutionMatrix();
        convolutionMatrix.setMatrix(MeanRemovalConfig);
        convolutionMatrix.Factor = 1;
        convolutionMatrix.Offset = 0;
        return convolutionMatrix.compute(bitmap);
    }
}
