package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by diman on 3/26/2016.
 */
public class Contrast implements IFilter {
    private double _value;

    public void configure(double value) {
        _value = value;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // image size
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // create output bitmap
        Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + _value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = bitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.red(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.red(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return result;
    }
}
