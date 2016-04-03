package com.dtodorov.darkroomnegative.services;

import android.content.ContentResolver;
import android.provider.Settings;

/**
 * Created by diman on 4/2/2016.
 */
public class Brightness implements IBrightness {
    private ContentResolver _contentResolver;
    private IPermissionService _permissionService;

    public Brightness(ContentResolver contentResolver, IPermissionService permissionService) {
        _contentResolver = contentResolver;
        _permissionService = permissionService;
    }

    private int getSettingsInt(String setting) throws Settings.SettingNotFoundException {
        return Settings.System.getInt(_contentResolver, setting);
    }

    private void setSettingsInt(String setting, int value) {
        if(_permissionService.getPermissionStatus(android.Manifest.permission.WRITE_SETTINGS) == IPermissionService.Status.Granted) {
            Settings.System.putInt(_contentResolver, setting, value);
        }
    }

    @Override
    public int getBrightness() {
        int currentBrightness = -1;
        try {
            currentBrightness = getSettingsInt(Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException ignore) {
        }
        return currentBrightness;
    }

    @Override
    public void setBrightness(int brightness) {
        if(brightness >= 0) {
            setSettingsInt(Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

    @Override
    public int getBrightnessMode() {
        int mode = -1;
        try {
            mode = getSettingsInt(Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException ignore) {
        }

        return mode;
    }

    @Override
    public void setBrightnessMode(int mode) {
        setSettingsInt(Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
    }


}
