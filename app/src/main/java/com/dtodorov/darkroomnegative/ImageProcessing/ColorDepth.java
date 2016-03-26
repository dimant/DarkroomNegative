package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by diman on 3/26/2016.
 */
public class ColorDepth implements IFilter {
    public enum BitOffset {
        BITS32,
        BITS64,
        BITS128
    }

    private int _offset;

    public void configure(BitOffset offset) {
        switch(offset) {
            case BITS32:
                _offset = 32;
                break;
            case BITS64:
                _offset = 64;
                break;
            case BITS128:
                _offset = 128;
                break;
            default:
                _offset = 0;
        }
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        // get image size
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

                // round-off color offset
                R = ((R + (_offset / 2))
                        - ((R + (_offset / 2)) % _offset) - 1);
                if (R < 0) {
                    R = 0;
                }
                G = ((G + (_offset / 2))
                        - ((G + (_offset / 2)) % _offset) - 1);
                if (G < 0) {
                    G = 0;
                }
                B = ((B + (_offset / 2))
                        - ((B + (_offset / 2)) % _offset) - 1);
                if (B < 0) {
                    B = 0;
                }

                // set pixel color to output bitmap
                result.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return result;
    }
}
