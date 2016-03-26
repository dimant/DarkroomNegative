package com.dtodorov.darkroomnegative;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Created by diman on 3/24/2016.
 */
public class Toaster implements IToaster {
    private Context _context;
    private Resources _resources;

    public Toaster(Context context, Resources resources)
    {
        _context = context;
        _resources = resources;
    }

    @Override
    public void Toast(int id)
    {
        String text = _resources.getString(id);
        this.Toast(text);
    }

    @Override
    public void Toast(String text)
    {
        Toast toast = Toast.makeText(_context, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
