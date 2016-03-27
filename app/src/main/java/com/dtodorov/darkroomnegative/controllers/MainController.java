package com.dtodorov.darkroomnegative.controllers;

import android.graphics.Bitmap;
import android.net.Uri;

import com.dtodorov.darkroomnegative.ImageProcessing.IAsyncFilterTask;
import com.dtodorov.darkroomnegative.ImageProcessing.IFilterCompletion;
import com.dtodorov.darkroomnegative.R;
import com.dtodorov.darkroomnegative.services.IBitmapLoader;
import com.dtodorov.darkroomnegative.services.IBitmapWriter;
import com.dtodorov.darkroomnegative.services.IClapDetector;
import com.dtodorov.darkroomnegative.services.IClapListener;
import com.dtodorov.darkroomnegative.services.IToaster;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ditodoro on 3/25/2016.
 */
public class MainController implements IFilterCompletion, IClapListener {
    private IToaster _toaster;
    private IBitmapLoader _bitmapLoader;
    private IAsyncFilterTask _greyscaleFilterTask;
    private IBitmapListener _bitmapListener;
    private IClapDetector _clapDetector;
    private Bitmap _bitmap;

    public MainController(
            IToaster toaster,
            IBitmapLoader bitmapLoader,
            IAsyncFilterTask greyscaleFilterTask,
            IBitmapListener bitmapListener,
            IClapDetector clapDetector
    )
    {
        _toaster = toaster;
        _bitmapLoader = bitmapLoader;
        _greyscaleFilterTask = greyscaleFilterTask;
        _greyscaleFilterTask.setCompletion(this);
        _bitmapListener = bitmapListener;
        _clapDetector = clapDetector;

        _clapDetector.setClapListener(this);
        _clapDetector.start();
    }

    public void setImage(Bitmap bitmap)
    {
        _bitmap = bitmap;
        _bitmapListener.onImageSet(_bitmap);
    }

    public void onImagePicked(Uri uri)
    {
        try {
            Bitmap bitmap =_bitmapLoader.Load(uri);
            _greyscaleFilterTask.apply(bitmap);
        } catch (FileNotFoundException e) {
            _toaster.Toast(R.string.error_image_read);
        }
    }

    @Override
    public void filterFinished(Bitmap bitmap) {
        setImage(bitmap);
    }

    @Override
    public void onClapped() {
        _toaster.Toast("Clap!");
    }
}
