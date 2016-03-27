package com.dtodorov.darkroomnegative.ImageProcessing;

import android.content.Context;
import android.support.v8.renderscript.RenderScript;

/**
 * Created by diman on 3/26/2016.
 */
public class RenderScriptContextFactory implements IRenderScriptContextFactory {
    private RenderScript _renderScript;

    public RenderScriptContextFactory(Context context) {
        _renderScript = RenderScript.create(context);
    }

    @Override
    public RenderScript getRenderScript() {
        return _renderScript;
    }
}
