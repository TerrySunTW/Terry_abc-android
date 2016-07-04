package com.abc.terry_sun.abc.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Terry on 2015/4/26.
 */
public class ScreenService {
    public static int GetScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static int GetScreenHeight()
    {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
