package com.dtodorov.darkroomnegative.helpers;

import android.content.res.Resources;

import com.dtodorov.darkroomnegative.R;

/**
 * Created by diman on 3/27/2016.
 */
public class UnitDisplayConverter implements IConverter<Integer, String, String> {

    @Override
    public String convert(Integer integer, String param) {
        return new String(integer + " " + param);
    }
}
