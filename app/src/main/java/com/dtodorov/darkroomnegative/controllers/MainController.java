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
import com.dtodorov.darkroomnegative.services.IExposerListener;
import com.dtodorov.darkroomnegative.services.IFullScreen;
import com.dtodorov.darkroomnegative.services.IFullScreenListener;
import com.dtodorov.darkroomnegative.services.IToaster;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action;

import java.io.FileNotFoundException;

import root.gast.audio.interp.ILoudNoiseListener;

/**
 * Created by ditodoro on 3/25/2016.
 */
public class MainController implements IClapListener, IExposerListener {
    private enum State {
        HomeScreen,
        ExposureSetup,
        FullScreen
    };

    public enum Trigger {
        Home,
        SetupExposure,
        Expose
    };

    private StateMachine<State, Trigger> _stateMachine;

    private IToaster _toaster;
    private IFullScreen _fullScreen;
    private IExposer _exposer;
    private IBitmapLoader _bitmapLoader;
    private IAsyncFilterTask _greyscaleFilterTask;
    private IAsyncFilterTask _negativeFilterTask;
    private IEventDispatcher _eventDispatcher;
    private IClapDetector _clapDetector;

    private Bitmap _positiveBitmap;
    private Bitmap _negativeBitmap;

    private int _exposureTime;
    private State _state;

    public MainController(
            IEventDispatcher eventDispatcher,
            IToaster toaster,
            IFullScreen fullScreen,
            IExposer exposer,
            IBitmapLoader bitmapLoader,
            IAsyncFilterTask greyscaleFilterTask,
            IAsyncFilterTask negativeFilterTask,
            IClapDetector clapDetector
    )
    {
        _eventDispatcher = eventDispatcher;
        _toaster = toaster;
        _fullScreen = fullScreen;
        _exposer = exposer;
        _bitmapLoader = bitmapLoader;
        _greyscaleFilterTask = greyscaleFilterTask;
        _negativeFilterTask = negativeFilterTask;
        _clapDetector = clapDetector;

        _clapDetector.setListener(this);
        _exposer.setListener(this);

        _stateMachine = new StateMachine<State, Trigger>(State.HomeScreen);

        _stateMachine.configure(State.HomeScreen)
                .permit(Trigger.SetupExposure, State.ExposureSetup)
                .permit(Trigger.Expose, State.FullScreen);

        _stateMachine.configure(State.ExposureSetup)
                .onEntry(new Action() {
                    @Override
                    public void doIt() {
                        _eventDispatcher.emit("showView", R.id.exposureTimeSeekBar);
                        _eventDispatcher.emit("showView", R.id.exposureTimeDisplay);
                    }
                })
                .onExit(new Action() {
                    @Override
                    public void doIt() {
                        _eventDispatcher.emit("hideView", R.id.exposureTimeSeekBar);
                        _eventDispatcher.emit("hideView", R.id.exposureTimeDisplay);
                    }
                })
                .permit(Trigger.Expose, State.FullScreen)
                .permit(Trigger.SetupExposure, State.HomeScreen);

        _stateMachine.configure(State.FullScreen)
                .onEntry(new Action() {
                    @Override
                    public void doIt() {
                        _eventDispatcher.emit("imageSet", _negativeBitmap);
                        _eventDispatcher.emit("hideView", R.id.imageView);
                        _eventDispatcher.emit("hideView", R.id.controlPanel);
                        _fullScreen.enterFullScreen();
                        _clapDetector.start();
                    }
                })
                .onExit(new Action() {
                    @Override
                    public void doIt() {
                        _clapDetector.stop();
                        _exposer.cancel();
                        _eventDispatcher.emit("imageSet", _positiveBitmap);
                        _fullScreen.exitFullScreen();
                    }
                })
                .permit(Trigger.Home, State.HomeScreen);

        _greyscaleFilterTask.setCompletion(new IFilterCompletion() {
            @Override
            public void filterFinished(Bitmap bitmap) {
                _positiveBitmap = bitmap;
                _eventDispatcher.emit("imageSet", _positiveBitmap);
                _negativeFilterTask.apply(bitmap);
            }
        });

        _negativeFilterTask.setCompletion(new IFilterCompletion() {
            @Override
            public void filterFinished(Bitmap bitmap) {
                _negativeBitmap = bitmap;
                _eventDispatcher.emit("enableView", R.id.beginExposureButton);
            }
        });

        _fullScreen.setFullScreenListener(new IFullScreenListener() {
            @Override
            public void onEnteredFullScreen() {

            }

            @Override
            public void onExitedFullScreen() {
                _eventDispatcher.emit("showView", R.id.imageView);
                _eventDispatcher.emit("showView", R.id.controlPanel);
            }
        });
    }

    public void fire(Trigger trigger) {
        if(_stateMachine.canFire(trigger)) {
            _stateMachine.fire(trigger);
        }
    }

    public void onImagePicked(Uri uri)
    {
        try {
            _positiveBitmap = null;
            _negativeBitmap = null;
            System.gc();

            _eventDispatcher.emit("imageSet", null);

            Bitmap bitmap =_bitmapLoader.Load(uri);
            _greyscaleFilterTask.apply(bitmap);
        } catch (FileNotFoundException e) {
            _toaster.Toast(R.string.error_image_read);
        }
    }

    public void restoreImages(Bitmap positiveBitmap, Bitmap negativeBitmap) {
        _positiveBitmap = positiveBitmap;
        _negativeBitmap = negativeBitmap;
        _eventDispatcher.emit("imageSet", _positiveBitmap);
        _eventDispatcher.emit("enableView", R.id.beginExposureButton);
    }

    public Bitmap getPositiveBitmap() {
        return _positiveBitmap;
    }

    public Bitmap getNegativeBitmap() {
        return _negativeBitmap;
    }

    public int getExposureTime() {
        return _exposureTime;
    }

    public void setExposureTime(int exposureTime) {
        _exposureTime = exposureTime;
        _eventDispatcher.emit("exposureTimeChanged", new Integer(exposureTime));
    }

    @Override
    public void onClap() {
        if(_stateMachine.getState() == State.FullScreen) {
            _exposer.expose(_exposureTime);
        }
    }

    @Override
    public void onExposeFinished() {
        if(_stateMachine.getState() == State.FullScreen) {
            _clapDetector.start();
        }
    }
}
