package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by diman on 3/26/2016.
 */
public class Brightness implements IFilter {
    private int _value;

    public void configure(int value) {
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

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = bitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += _value;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }

                G += _value;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }

                B += _value;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }

                // apply new pixel color to output bitmap
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return result;
    }
}
