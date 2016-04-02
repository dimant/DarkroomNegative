package com.dtodorov.darkroomnegative.services;

/**
 * Created by ditodoro on 4/1/2016.
 */
public interface IPermissionService {
    void obtainPermission(String permission);

    Status getPermissionStatus(String permission);

    public enum Status {
        Granted,
        Denied,
        Unknown
    }
}
