package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class Boost implements IFilter {
    public enum Type {
        RED,
        GREEN,
        BLUE
    }

    private Type _type;
    private float _percent;

    public void configure(Type type, float percent) {
        _type = type;
        _percent = percent;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());

        int A, R, G, B;
        int pixel;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixel = bitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                switch (_type) {
                    case RED:
                        R = (int) (R * (1 + _percent));
                        if (R > 255)
                            R = 255;
                        break;
                    case GREEN:
                        G = (int) (G * (1 + _percent));
                        if (G > 255)
                            G = 255;
                        break;
                    case BLUE:
                        B = (int) (B * (1 + _percent));
                        if (B > 255)
                            B = 255;
                        break;
                }

                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return result;
    }
}
