package com.dtodorov.darkroomnegative.services;

/**
 * Created by ditodoro on 4/1/2016.
 */
public interface IPermissionRequester {
    void setListener(IPermissionListener listener);
    void obtainPermission(String permission);
}
