package com.dtodorov.darkroomnegative.services;

import android.os.AsyncTask;

import root.gast.audio.interp.ILoudNoiseListener;
import root.gast.audio.interp.LoudNoiseDetector;
import root.gast.audio.interp.LoudNoiseDetectorAboveNormal;
import root.gast.audio.record.AudioClipRecorder;
import root.gast.audio.record.IAudioClipRecorder;

/**
 * Created by diman on 3/31/2016.
 */
public class ClapDetector implements IClapDetector {
    private AsyncTask<Void, Void, Void> _task;
    IAudioClipRecorder _recorder;
    IClapListener _listener;

    public ClapDetector() {}

    public ClapDetector(IClapListener listener) {
        _listener = listener;
    }

    @Override
    public void setListener(IClapListener listener) {
        _listener = listener;
    }

    @Override
    public void start() {
        _task = new AsyncTask<Void, Void, Void>() {
            private IAudioClipRecorder _recorder;

            @Override
            protected Void doInBackground(Void... params) {

                _recorder = new AudioClipRecorder(
                    new LoudNoiseDetector(new ILoudNoiseListener() {
                        @Override
                        public void heard() {
                            if (_listener != null) {
                                _listener.onClap();
                            }
                            _recorder.start();
                        }
                    })
                );
                _recorder.start();
                return null;
            }
        };
        _task.execute();
    }

    @Override
    public void stop() {
        if(_recorder != null)
            _recorder.stop();
        _recorder = null;

        if(_task != null) {
            if(_task.isCancelled() == false) {
                _task.cancel(true);
            }
            _task = null;
        }
    }
}
