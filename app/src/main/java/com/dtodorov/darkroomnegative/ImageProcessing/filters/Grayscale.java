package com.dtodorov.darkroomnegative.ImageProcessing.filters;

import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptC;

import com.dtodorov.darkroomnegative.ImageProcessing.IFilter;
import com.dtodorov.darkroomnegative.ImageProcessing.IRenderScriptContextFactory;

/**
 * Created by diman on 3/26/2016.
 */
public class Grayscale extends RenderScriptFilter {
    public Grayscale(IRenderScriptContextFactory renderScriptContextFactory) {
        super(renderScriptContextFactory);
    }

    @Override
    protected void runScriptC(Allocation inputAllocation, Allocation outputAllocation) {
        ScriptC_Grayscale grayscale = new ScriptC_Grayscale(getRenderScript());
        grayscale.forEach_root(inputAllocation, outputAllocation);
    }

}
