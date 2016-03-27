package com.dtodorov.darkroomnegative.controllers;

import android.graphics.Bitmap;
import android.net.Uri;

import com.dtodorov.darkroomnegative.ImageProcessing.IAsyncFilterTask;
import com.dtodorov.darkroomnegative.ImageProcessing.IFilterCompletion;
import com.dtodorov.darkroomnegative.R;
import com.dtodorov.darkroomnegative.helpers.IEventDispatcher;
import com.dtodorov.darkroomnegative.services.IBitmapLoader;
import com.dtodorov.darkroomnegative.services.IClapDetector;
import com.dtodorov.darkroomnegative.services.IClapListener;
import com.dtodorov.darkroomnegative.services.IExposer;
import com.dtodorov.darkroomnegative.services.IFullScreen;
import com.dtodorov.darkroomnegative.services.IFullScreenListener;
import com.dtodorov.darkroomnegative.services.IToaster;

import java.io.FileNotFoundException;

/**
 * Created by ditodoro on 3/25/2016.
 */
public class MainController implements IFilterCompletion, IClapListener, IFullScreenListener {
    private enum State {
        HomeScreen,
        ExposureSetup,
        FullScreen
    };

    private IToaster _toaster;
    private IFullScreen _fullScreen;
    private IExposer _exposer;
    private IBitmapLoader _bitmapLoader;
    private IAsyncFilterTask _greyscaleFilterTask;
    private IClapDetector _clapDetector;
    private IEventDispatcher _eventDispatcher;
    private Bitmap _bitmap;

    private int _exposureTime;
    private State _state;

    public MainController(
            IEventDispatcher eventDispatcher,
            IToaster toaster,
            IFullScreen fullScreen,
            IExposer exposer,
            IBitmapLoader bitmapLoader,
            IAsyncFilterTask greyscaleFilterTask,
            IClapDetector clapDetector
    )
    {
        _eventDispatcher = eventDispatcher;
        _toaster = toaster;
        _fullScreen = fullScreen;
        _exposer = exposer;
        _bitmapLoader = bitmapLoader;
        _greyscaleFilterTask = greyscaleFilterTask;
        _clapDetector = clapDetector;

        _fullScreen.setFullScreenListener(this);
        _greyscaleFilterTask.setCompletion(this);
        _clapDetector.setClapListener(this);
        _clapDetector.start();

        _state = State.HomeScreen;
    }

    public void setImage(Bitmap bitmap)
    {
        _bitmap = bitmap;
        _eventDispatcher.emit("imageSet", _bitmap);
    }

    public void setExposureTime(int exposureTime) {
        _exposureTime = exposureTime;
        _eventDispatcher.emit("exposureTimeChanged", new Integer(exposureTime));
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

    public void enterExposeImage() {
        if(_state == State.HomeScreen) {
            _eventDispatcher.emit("hideView", R.id.imageView);
            _eventDispatcher.emit("hideView", R.id.exposureTimeDisplay);
            _eventDispatcher.emit("hideView", R.id.controlPanel);
            _fullScreen.enterFullScreen();
        }
    }

    public void exitExposeImage() {
        if(_state == State.FullScreen) {
            _exposer.cancel();
            _fullScreen.exitFullScreen();
            _eventDispatcher.emit("showView", R.id.imageView);
            _eventDispatcher.emit("showView", R.id.controlPanel);
        }
    }

    public void setupExposureTime() {
        switch(_state) {
            case HomeScreen:
                _eventDispatcher.emit("showView", R.id.exposureTimeSeekBar);
                _eventDispatcher.emit("showView", R.id.exposureTimeDisplay);
                _state = State.ExposureSetup;
                break;
            case ExposureSetup:
                _eventDispatcher.emit("hideView", R.id.exposureTimeSeekBar);
                _eventDispatcher.emit("hideView", R.id.exposureTimeDisplay);
                _state = State.HomeScreen;
                break;
        }
   }

    @Override
    public void filterFinished(Bitmap bitmap) {
        setImage(bitmap);
        _eventDispatcher.emit("enableView", R.id.beginExposureButton);
    }

    @Override
    public void onClapped() {
        switch(_state) {
            case HomeScreen:
                _toaster.Toast("Clap!");
                break;
            case FullScreen:
                _exposer.expose(_exposureTime);
                break;
        }
    }

    @Override
    public void onEnteredFullScreen() {
        _state = State.FullScreen;
    }

    @Override
    public void onExitedFullScreen() {
        _state = State.HomeScreen;
    }
}
