package com.dtodorov.darkroomnegative.services;

/**
 * Created by ditodoro on 4/1/2016.
 */
public interface IPermissionService {
    void obtainPermission(String permission, String explanation);

    Status getPermissionStatus(String permission);

    public enum Status {
        Granted,
        Denied
    }
}
