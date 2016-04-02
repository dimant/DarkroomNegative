package com.dtodorov.darkroomnegative.services;

import android.content.ContentResolver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;

import java.util.Set;

/**
 * Created by diman on 3/27/2016.
 */
public class Exposer implements IExposer {
    private Handler _handler;
    private View _view;
    private ContentResolver _contentResolver;
    private int _currentBrightness;
    private int _currentBrightnessMode;
    private IExposerListener _listener;
    private boolean _changeBrightness;

    public Exposer(View view, ContentResolver contentResolver) {
        _view = view;
        _contentResolver = contentResolver;
        _handler = new Handler(Looper.getMainLooper());
    }

    public void setListener(IExposerListener listener) {
        _listener = listener;
    }

    private int getSettingsInt(String setting) throws Settings.SettingNotFoundException {
        return Settings.System.getInt(_contentResolver, setting);
    }

    private void setSettingsInt(String setting, int value) {
        if(_changeBrightness) {
            Settings.System.putInt(_contentResolver, setting, value);
        }
    }

    private int getBrightness() {
        int currentBrightness = -1;
        try {
            currentBrightness = getSettingsInt(Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException ignore) {
        }
        return currentBrightness;
    }

    private void setBrightness(int brightness) {
        if(brightness >= 0) {
            setSettingsInt(Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

    private int getBrightnessMode() {
        int mode = -1;
        try {
            mode = getSettingsInt(Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException ignore) {
        }

        return mode;
    }

    private void setBrightnessMode(int mode) {
        setSettingsInt(Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
    }

    @Override
    public void expose(int seconds, final boolean changeBrightness) {
        _changeBrightness = changeBrightness;
        _handler.post(new Runnable() {
            @Override
            public void run() {
                _currentBrightnessMode = getBrightnessMode();
                _currentBrightness = getBrightness();
                setBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                setBrightness(255);
                _view.setVisibility(View.VISIBLE);
            }
        });

        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBrightness(_currentBrightness);
                setBrightnessMode(_currentBrightnessMode);
                _view.setVisibility(View.INVISIBLE);
                _listener.onExposeFinished();
            }
        }, seconds * 1000);
    }

    @Override
    public void cancel() {
        _handler.removeCallbacksAndMessages(null);
        setBrightness(_currentBrightness);
        setBrightnessMode(_currentBrightnessMode);
    }
}
