package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class ColorFilter implements IFilter {
    private double _red;
    private double _green;
    private double _blue;

    public void configure(double red, double green, double blue){
        _red = red;
        _green = green;
        _blue = blue;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
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
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int) (Color.red(pixel) * _red);
                G = (int) (Color.green(pixel) * _green);
                B = (int) (Color.blue(pixel) * _blue);
                // set new color pixel to output bitmap
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return result;    }
}
