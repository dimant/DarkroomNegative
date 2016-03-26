package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;

/**
 * Created by diman on 3/26/2016.
 */
public class ReplaceColor implements IFilter {
    private int _fromColor;
    private int _toColor;

    public void configure(int fromColor, int toColor) {
        _fromColor = fromColor;
        _toColor = toColor;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int x = 0; x < pixels.length; ++x) {
            pixels[x] = (pixels[x] == _fromColor) ? _toColor : pixels[x];
        }

        Bitmap newImage = Bitmap
                .createBitmap(width, height, bitmap.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return newImage;    }
}
