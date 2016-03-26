package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by diman on 3/26/2016.
 */
public class Greyscale implements IFilter {

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                bitmap.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // scan through every single pixel
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = bitmap.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return result;
    }
}
