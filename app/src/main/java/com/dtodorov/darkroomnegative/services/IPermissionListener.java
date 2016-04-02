package com.dtodorov.darkroomnegative.services;

/**
 * Created by ditodoro on 4/1/2016.
 */
public interface IPermissionListener {
    void onGranted(String permission);
    void onDenied(String permission);
    void onCancelled(String permission);
}
