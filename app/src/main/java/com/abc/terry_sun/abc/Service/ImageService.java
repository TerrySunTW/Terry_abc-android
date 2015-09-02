package com.abc.terry_sun.abc.Service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.orm.SugarApp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by terry_sun on 2015/7/1.
 */
public class ImageService {
    public static String GetImageFileName(String Image)
    {
        String ImageName=Image + ".jpg";
        if(!Image.endsWith(".jpg")) {
            return ImageName;
        }
        return Image;
    }
    public static Bitmap GetBitmapFromImageName(String ImageName)
    {
        return BitmapFactory.decodeFile(StorageService.GetImagePath(ImageName));
    }

    public static Bitmap GetBitmapFromPath(String Path)
    {
        return BitmapFactory.decodeFile(Path);
    }

}
