package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by diman on 3/26/2016.
 */
public class FleaEffect implements IFilter {
    private final int COLOR_MAX = 0xFF;

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // get image size
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        // a random object
        Random random = new Random();

        int index = 0;
        // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // get random color
                int randColor = Color.rgb(random.nextInt(COLOR_MAX),
                        random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
                // OR
                pixels[index] |= randColor;
            }
        }
        // output bitmap
        Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;    }
}
