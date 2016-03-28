package com.dtodorov.darkroomnegative.services;

/**
 * Created by diman on 3/26/2016.
 */
public interface IClapDetector {
    void setClapListener(IClapListener clapListener);
    void start();
}
