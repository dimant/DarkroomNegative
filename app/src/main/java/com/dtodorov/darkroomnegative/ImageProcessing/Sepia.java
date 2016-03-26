package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by diman on 3/26/2016.
 */
public class Sepia implements IFilter {
    private double _red;
    private double _green;
    private double _blue;
    private double _depth;

    public void config(double red, double green, double blue, double depth) {
        _red = red;
        _green = green;
        _blue = blue;
        _depth = depth;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // image size
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // create output bitmap
        Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
        // constant greyscale
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = bitmap.getPixel(x, y);
                // get color on each channel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // apply greyscale sample
                B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // apply intensity level for sepia-toning on each channel
                R += (_depth * _red);
                if (R > 255) {
                    R = 255;
                }

                G += (_depth * _green);
                if (G > 255) {
                    G = 255;
                }

                B += (_depth * _blue);
                if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output image
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return result;
    }
}
