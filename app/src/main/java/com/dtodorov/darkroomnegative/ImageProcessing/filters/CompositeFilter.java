package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diman on 3/28/2016.
 */
public class CompositeFilter implements IFilter {
    private List<IFilter> _filters;

    public CompositeFilter() {
        _filters = new ArrayList<IFilter>();
    }

    public CompositeFilter(List<IFilter> filters) {
        _filters = filters;
    }

    public void addFilter(IFilter filter) {
        _filters.add(filter);
    }

    public Bitmap apply(Bitmap bitmap) {
        Bitmap result = bitmap;

        for(IFilter filter : _filters) {
            result = filter.apply(result);
        }

        return result;
    }
}
