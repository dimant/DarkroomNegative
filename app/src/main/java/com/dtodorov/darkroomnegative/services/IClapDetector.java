package com.dtodorov.darkroomnegative.services;

/**
 * Created by diman on 3/31/2016.
 */
public interface IClapDetector {
    void setListener(IClapListener listener);

    void start();

    void stop();
}
