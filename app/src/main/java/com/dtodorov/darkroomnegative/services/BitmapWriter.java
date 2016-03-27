package com.dtodorov.darkroomnegative.services;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by diman on 3/26/2016.
 */
public class BitmapWriter implements IBitmapWriter {
    private Context _context;

    public BitmapWriter(Context context) {
        _context = context;
    }

    @Override
    public void Write(Bitmap bitmap, String filename) throws IOException {
        FileOutputStream stream = _context.openFileOutput(filename, Context.MODE_PRIVATE);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.close();
    }
}
