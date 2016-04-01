package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptC;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;
import com.dtodorov.darkroomnegative.ImageProcessing.IRenderScriptContextFactory;

/**
 * Created by diman on 3/28/2016.
 */
public abstract class RenderScriptFilter implements IFilter {
    private RenderScript _renderScript;

    public RenderScriptFilter(IRenderScriptContextFactory renderScriptContextFactory) {
        _renderScript = renderScriptContextFactory.getRenderScript();
    }

    protected RenderScript getRenderScript() {
        return _renderScript;
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation inputAllocation = Allocation.createFromBitmap(_renderScript, bitmap);
        Allocation outputAllocation = Allocation.createFromBitmap(_renderScript, result);

        runScriptC(inputAllocation, outputAllocation);

        outputAllocation.copyTo(result);

        inputAllocation.destroy();
        outputAllocation.destroy();

        return result;
    }

    protected abstract void runScriptC(Allocation inputAllocation, Allocation outputAllocation);
}
