package com.dtodorov.darkroomnegative.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;


import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;

/**
 * Created by diman on 3/26/2016.
 */
public class ClapDetector implements IClapDetector {
    private Thread _thread;
    private IClapListener _clapListener;
    private Handler _handler;

    private Runnable _callback = new Runnable() {

        @Override
        public void run() {
            _clapListener.onClapped();
        }
    };

    public ClapDetector() {
        _handler = new Handler(Looper.getMainLooper());

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        double threshold = 8;
        double sensitivity = 20;
        PercussionOnsetDetector mPercussionDetector = new PercussionOnsetDetector(22050, 1024,
                new OnsetHandler() {

                    @Override
                    public void handleOnset(double time, double salience) {
                        if(_clapListener != null) {
                            _handler.post(_callback);
                        }
                    }
                }, sensitivity, threshold);
        dispatcher.addAudioProcessor(mPercussionDetector);
        _thread = new Thread(dispatcher);
    }

    @Override
    public void start() {
        _thread.start();
    }

    @Override
    public void setClapListener(IClapListener clapListener) {
        _clapListener = clapListener;
    }
}