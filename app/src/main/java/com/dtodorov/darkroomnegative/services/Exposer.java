package com.dtodorov.darkroomnegative.services;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;

/**
 * Created by diman on 3/27/2016.
 */
public class Exposer implements IExposer {
    private Handler _handler;
    private View _view;
    private IExposerListener _listener;
    private IBrightness _brightness;

    public Exposer(View view, IBrightness backlight) {
        _view = view;
        _brightness = backlight;
        _handler = new Handler(Looper.getMainLooper());
    }

    public void setListener(IExposerListener listener) {
        _listener = listener;
    }

    @Override
    public void expose(int seconds) {
        _brightness.setBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

        _handler.post(new Runnable() {
            @Override
            public void run() {
                _brightness.setBrightness(255);
                _view.setVisibility(View.VISIBLE);
            }
        });

        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _brightness.setBrightness(0);
                _view.setVisibility(View.INVISIBLE);
                _listener.onExposeFinished();
            }
        }, seconds * 1000);
    }

    @Override
    public void cancel() {
        _handler.removeCallbacksAndMessages(null);
    }
}
