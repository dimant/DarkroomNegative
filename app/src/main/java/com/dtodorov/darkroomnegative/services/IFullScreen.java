package com.dtodorov.darkroomnegative.services;

/**
 * Created by diman on 3/27/2016.
 */
public interface IFullScreen {
    void setFullScreenListener(IFullScreenListener fullScreenListener);

    void enterFullScreen();

    void exitFullScreen();
}
