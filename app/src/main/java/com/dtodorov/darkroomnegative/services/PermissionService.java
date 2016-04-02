package com.dtodorov.darkroomnegative.services;

import android.content.pm.PackageManager;

import java.util.Map;

/**
 * Created by ditodoro on 4/1/2016.
 */
public class PermissionService implements IPermissionListener, IPermissionService {

    private Map<String, Integer> _permissions;
    private IPermissionRequester _requester;

    public PermissionService(IPermissionRequester requester) {
        _requester = requester;
        _requester.setListener(this);
    }

    @Override
    public void obtainPermission(String permission) {
        _requester.obtainPermission(permission);
    }

    @Override
    public Status getPermissionStatus(String permission) {
        if(_permissions.containsKey(permission)) {
            if(_permissions.get(permission) == PackageManager.PERMISSION_GRANTED) {
                return Status.Granted;
            } else {
                return Status.Denied;
            }
        } else {
            return  Status.Unknown;
        }
    }

    @Override
    public void onGranted(String permission) {
        _permissions.put(permission, PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onDenied(String permission) {
        _permissions.put(permission, PackageManager.PERMISSION_DENIED);
    }

    @Override
    public void onCancelled(String permission) {
        _permissions.put(permission, PackageManager.PERMISSION_DENIED);
    }
}
