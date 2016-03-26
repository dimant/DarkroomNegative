package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;

/**
 * Created by diman on 3/26/2016.
 */
public interface IAsyncFilterTask {
    void apply(Bitmap bitmap);

    void setCompletion(IFilterCompletion completion);
}
