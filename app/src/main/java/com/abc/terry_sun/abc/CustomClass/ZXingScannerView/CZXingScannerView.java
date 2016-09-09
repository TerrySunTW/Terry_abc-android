package com.abc.terry_sun.abc.CustomClass.ZXingScannerView;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

import me.dm7.barcodescanner.core.BarcodeScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by terry_sun on 8/16/2016.
 */
public class CZXingScannerView extends ZXingScannerView {
    static String TAG="CZXingScannerView";
    public CZXingScannerView(Context context) {
        super(context);
    }

    public CZXingScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    public void onPreviewFrame(byte[] data, Camera camera) {
        /**
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(parameters.FOCUS_MODE_MACRO)) {
            Log.i(TAG,"focusModes.contains");
            parameters.setFocusMode(parameters.FOCUS_MODE_MACRO);
        }
        else
        {
            Log.i(TAG,"focusModes.contains--NONO");
        }
        camera.setParameters(parameters);
         **/
        super.onPreviewFrame(data, camera);
    }
}
