package com.dtodorov.darkroomnegative.controllers;

import android.graphics.Bitmap;
import android.net.Uri;

import com.dtodorov.darkroomnegative.R;
import com.dtodorov.darkroomnegative.services.IBitmapLoader;
import com.dtodorov.darkroomnegative.services.IToaster;

import java.io.FileNotFoundException;

/**
 * Created by ditodoro on 3/25/2016.
 */
public class MainController {
    private IToaster _toaster;
    private IBitmapLoader _bitmapLoader;
    private IBitmapListener _bitmapListener;

    private Bitmap _bitmap;

    public MainController(
            IToaster toaster,
            IBitmapLoader bitmapLoader,
            IBitmapListener bitmapListener
    )
    {
        _toaster = toaster;
        _bitmapLoader = bitmapLoader;
        _bitmapListener = bitmapListener;
    }

    public void onImagePicked(Uri uri)
    {
        try {
            _bitmap = _bitmapLoader.Load(uri);
            _bitmapListener.onBitmapChanged(_bitmap);
        } catch (FileNotFoundException e) {
            _toaster.Toast(R.string.error_image_open);
        }
    }
}
