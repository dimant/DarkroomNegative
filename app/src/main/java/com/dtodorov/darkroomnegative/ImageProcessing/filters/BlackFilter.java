package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

import java.util.Random;

/**
 * Created by diman on 3/26/2016.
 */
public class BlackFilter implements IFilter {
    private final int COLOR_MIN = 0x00;
    private final int COLOR_MAX = 0xFF;

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // get image size
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        // random object
        Random random = new Random();

        int R, G, B, index = 0, threshold = 0;
        // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // get color
                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);
                // generate threshold
                threshold = random.nextInt(COLOR_MAX);
                if (R < threshold && G < threshold && B < threshold) {
                    pixels[index] = Color.rgb(COLOR_MIN, COLOR_MIN, COLOR_MIN);
                }
            }
        }
        // output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;    }
}
