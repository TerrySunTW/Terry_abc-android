package com.abc.terry_sun.abc.Service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.orm.SugarApp;

/**
 * Created by terry_sun on 2015/7/1.
 */
public class ImageService {
    public static String GetImageFileName(String Image)
    {
        return Image + ".jpg";
    }
    public static Bitmap GetBitmapFromImageName(String ImageName)
    {
        return BitmapFactory.decodeFile(StorageService.GetImagePath(ImageName));
    }
}
