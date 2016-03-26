package com.dtodorov.darkroomnegative.ImageProcessing;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by diman on 3/26/2016.
 */
public class AsyncFilterTask implements IAsyncFilterTask {
    private IFilter _filter;
    private IFilterCompletion _completion;

    public AsyncFilterTask(IFilter filter) {
        _filter = filter;
    }

    @Override
    public void apply(Bitmap bitmap) {
        AsyncTask<Bitmap, Void, Bitmap> task = new AsyncTask<Bitmap, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Bitmap... params) {
                return _filter.apply(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(_completion != null) {
                    _completion.filterFinished(bitmap);
                }
            }
        };
        task.execute(bitmap);
    }

    @Override
    public void setCompletion(IFilterCompletion completion) {
        _completion = completion;
    }
}
