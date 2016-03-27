package com.dtodorov.darkroomnegative.services;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.os.Handler;

/**
 * Created by diman on 3/26/2016.
 */
public class FullScreen {
    private final Handler mHideHandler = new Handler();
    private static final int UI_ANIMATION_DELAY = 300;

    private AppCompatActivity _activity;
    private View _contentControl;
    private View[] _controlsToHide;

    public  FullScreen(
            AppCompatActivity activity,
            View contentControl,
            View[] controlsoHide) {
        _activity = activity;
        _contentControl = contentControl;
        _controlsToHide = controlsoHide;
    }

    public void enterFullScreen() {
        ActionBar actionBar = _activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        for(View view : _controlsToHide) {
            view.setVisibility(View.GONE);
        }

        mHideHandler.removeCallbacks(_exitFullScreenPart2);
        mHideHandler.postDelayed(_enterFullScreenPart2, UI_ANIMATION_DELAY);
    }

    private final Runnable _enterFullScreenPart2 = new Runnable() {
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            _contentControl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    public void exitFullScreen() {
        // Show the system bar
        _contentControl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(_enterFullScreenPart2);
        mHideHandler.postDelayed(_exitFullScreenPart2, UI_ANIMATION_DELAY);
    }

    private final Runnable _exitFullScreenPart2 = new Runnable() {
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            _contentControl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
}