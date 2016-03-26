package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class Invert implements IFilter {
    @Override
    public Bitmap apply(Bitmap bitmap) {
        // create new bitmap with the same settings as source bitmap
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                bitmap.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // get one pixel
                pixelColor = bitmap.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // set newly-inverted pixel to output image
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final bitmap
        return result;    }
}
