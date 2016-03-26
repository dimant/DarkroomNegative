package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

/**
 * Created by diman on 3/26/2016.
 */
public class WaterMark implements IFilter {

    private Point _location;
    private String _text;
    int _color;
    int _alpha;
    int _size;
    boolean _underline;

    public void configure(
            String text,
            Point location,
            int color,
            int alpha,
            int size,
            boolean underline) {
        _text = text;
        _location = location;
        _color = color;
        _alpha = alpha;
        _size = size;
        _underline = underline;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, bitmap.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(_color);
        paint.setAlpha(_alpha);
        paint.setTextSize(_size);
        paint.setAntiAlias(true);
        paint.setUnderlineText(_underline);
        canvas.drawText(_text, _location.x, _location.y, paint);

        return result;
    }
}
