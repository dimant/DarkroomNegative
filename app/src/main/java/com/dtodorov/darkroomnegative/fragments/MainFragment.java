package com.dtodorov.darkroomnegative.fragments;

import android.app.Fragment;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diman on 3/28/2016.
 */
public class MainFragment extends Fragment {
    public static final String NEGATIVE_IMAGE = "negativeImage";
    public static final String POSITIVE_IMAGE = "positiveImage";
    public static final String EXPOSURE_TIME = "exposureTime";

    private Map<String, Object> _retainedData;

    public MainFragment() {
        _retainedData = new HashMap<String, Object>();
    }

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void putObject(String key, Object value) {
        _retainedData.put(key, value);
    }

    public <T> T getObject(String key) {
        return (T) _retainedData.get(key);
    }
}
