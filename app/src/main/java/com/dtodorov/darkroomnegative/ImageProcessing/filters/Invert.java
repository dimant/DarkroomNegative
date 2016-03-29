package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;
import com.dtodorov.darkroomnegative.ImageProcessing.IRenderScriptContextFactory;

/**
 * Created by diman on 3/26/2016.
 */
public class Invert implements IFilter {

    private RenderScript _renderScript;

    public Invert(IRenderScriptContextFactory renderScriptContextFactory) {
        _renderScript = renderScriptContextFactory.getRenderScript();
    }

    @Override
    public Bitmap apply(Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation inputAllocation = Allocation.createFromBitmap(_renderScript, bitmap);
        Allocation outputAllocation = Allocation.createFromBitmap(_renderScript, result);

        ScriptC_Invert invert = new ScriptC_Invert(_renderScript);

        invert.forEach_root(inputAllocation, outputAllocation);
        outputAllocation.copyTo(result);

        return result;
    }
}
