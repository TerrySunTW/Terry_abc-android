package com.abc.terry_sun.abc.Service;

import android.content.Context;
import android.os.Environment;

import com.abc.terry_sun.abc.R;

import java.io.File;
import java.security.PublicKey;

/**
 * Created by terry_sun on 2015/7/1.
 */
public class StorageService {
    private static String FolderName="ABC/";
    public static String GetImagePath(Context context,String ImageName)
    {
        return GetAppStoragePath(context)+ File.separator+ImageName;
    }
    public static String GetAppStoragePath(Context context)
    {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {

            File podcastFolder = new File(Environment.getExternalStorageDirectory() + File.separator
                    + context.getString(R.string.app_name));
            return podcastFolder.getAbsolutePath();
        } else {
            /* save the folder in internal memory of phone */

            File podcastFolder = new File("/data/data/" + context.getPackageName()
                    + File.separator + context.getString(R.string.app_name));
            return podcastFolder.getAbsolutePath();
        }
    }
    public static void GetAppStorageFolderInitial(Context context)
    {
        File f = new File(GetAppStoragePath(context));
        if(!f.exists()) {
            f.mkdirs();
        }
    }
}
