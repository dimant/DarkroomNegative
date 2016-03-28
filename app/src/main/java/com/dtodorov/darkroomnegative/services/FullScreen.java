package com.dtodorov.darkroomnegative.services;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.os.Handler;

/**
 * Created by diman on 3/26/2016.
 */
public class FullScreen implements IFullScreen {
    private final Handler mHideHandler = new Handler();
    private static final int UI_ANIMATION_DELAY = 300;

    private AppCompatActivity _activity;
    private View _contentControl;

    private IFullScreenListener _fullScreenListener;

    public  FullScreen(
            AppCompatActivity activity,
            View contentControl) {
        _activity = activity;
        _contentControl = contentControl;
    }

    @Override
    public void setFullScreenListener(IFullScreenListener fullScreenListener) {
        _fullScreenListener = fullScreenListener;
    }

    @Override
    public void enterFullScreen() {
        ActionBar actionBar = _activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mHideHandler.removeCallbacksAndMessages(null);
        mHideHandler.postDelayed(_enterFullScreenPart2, UI_ANIMATION_DELAY);
    }

    private final Runnable _enterFullScreenPart2 = new Runnable() {
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            _contentControl.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);

            if(_fullScreenListener != null) {
                _fullScreenListener.onEnteredFullScreen();
            }
        }
    };

    @Override
    public void exitFullScreen() {
        ActionBar actionBar = _activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacksAndMessages(null);
        mHideHandler.postDelayed(_exitFullScreenPart2, UI_ANIMATION_DELAY);
    }

    private final Runnable _exitFullScreenPart2 = new Runnable() {
        @Override
        public void run() {
            // Show the system bar
            _contentControl.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            if(_fullScreenListener != null) {
                _fullScreenListener.onExitedFullScreen();
            }
        }
    };
}
