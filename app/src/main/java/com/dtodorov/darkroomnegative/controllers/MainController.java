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
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action;

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
                        _eventDispatcher.emit("hideView", R.id.imageView);
                        _eventDispatcher.emit("hideView", R.id.controlPanel);
                        _fullScreen.enterFullScreen();
                    }
                })
                .onExit(new Action() {
                    @Override
                    public void doIt() {
                        _fullScreen.exitFullScreen();
                        _exposer.cancel();
                    }
                })
                .permit(Trigger.Home, State.HomeScreen);

        _greyscaleFilterTask.setCompletion(this);
        _fullScreen.setFullScreenListener(this);
        _clapDetector.setClapListener(this);
        _clapDetector.start();
    }

    public void fire(Trigger trigger) {
        if(_stateMachine.canFire(trigger)) {
            _stateMachine.fire(trigger);
        }
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

    @Override
    public void filterFinished(Bitmap bitmap) {
        setImage(bitmap);
        _eventDispatcher.emit("enableView", R.id.beginExposureButton);
    }

    @Override
    public void onClapped() {
        if(_stateMachine.getState() == State.FullScreen) {
            _exposer.expose(_exposureTime);
        }
    }

    @Override
    public void onEnteredFullScreen() {

    }

    @Override
    public void onExitedFullScreen() {
        _eventDispatcher.emit("showView", R.id.imageView);
        _eventDispatcher.emit("showView", R.id.controlPanel);
    }

}
