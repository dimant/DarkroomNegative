package com.dtodorov.darkroomnegative.services;

import android.os.Handler;
import android.view.View;

/**
 * Created by diman on 3/27/2016.
 */
public class Exposer implements IExposer {
    private Handler _handler;
    private View _view;

    public Exposer(View view) {
        _view = view;
    }

    @Override
    public void expose(int seconds) {
        _view.setVisibility(View.VISIBLE);
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _view.setVisibility(View.INVISIBLE);
            }
        }, seconds * 1000);
    }

    @Override
    public void cancel() {
        _handler.removeCallbacksAndMessages(null);
    }
}
