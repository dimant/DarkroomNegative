package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class Transparency implements IFilter {
    private int _eraseColor;

    public void configure(int eraseColor) {
        _eraseColor = eraseColor;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.eraseColor(_eraseColor);

        Canvas c = new Canvas(result);

        Bitmap alpha = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] alphaPix = new int[width * height];
        bitmap.getPixels(alphaPix, 0, width, 0, 0, width, height);

        int count = width * height;
        for (int i = 0; i < count; ++i) {
            // Usamos el canal rojo como alpha
            alphaPix[i] = alphaPix[i] << 8; // Realizamos desplazamiento a la
            // izquierda con signo
        }

        alpha.setPixels(alphaPix, 0, width, 0, 0, width, height);

        Paint alphaP = new Paint();
        alphaP.setAntiAlias(true);
        alphaP.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        c.drawBitmap(alpha, 0, 0, alphaP);

        bitmap.recycle();
        alpha.recycle();

        return result;
    }
}
