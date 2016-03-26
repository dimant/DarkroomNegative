package com.dtodorov.darkroomnegative.services;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by ditodoro on 3/25/2016.
 */
public class BitmapLoader implements IBitmapLoader {
    private Context _context;

    public BitmapLoader(Context context)
    {
        _context = context;
    }

    @Override
    public Bitmap Load(Uri uri) throws FileNotFoundException
    {
        ContentResolver contentResolver = _context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
