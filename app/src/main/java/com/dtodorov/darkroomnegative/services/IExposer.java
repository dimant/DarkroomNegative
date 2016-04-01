package com.dtodorov.darkroomnegative.services;

/**
 * Created by diman on 3/27/2016.
 */
public interface IExposer {
    void setListener(IExposerListener listener);

    void expose(int seconds);

    void cancel();
}
