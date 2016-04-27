package com.abc.terry_sun.abc.Service;

import android.content.Context;
import android.os.Environment;

import com.abc.terry_sun.abc.CustomClass.Application.ABCApplication;
import com.abc.terry_sun.abc.R;

import java.io.File;
import java.security.PublicKey;

/**
 * Created by terry_sun on 2015/7/1.
 */
public class StorageService {
    private static String FolderName="ABC/";
    public static String GetImagePath(String ImageName)
    {
        return GetAppStoragePath()+ File.separator+ImageService.GetImageFileName(ImageName);
    }
    public static String GetFriendImagePath(String FriendID)
    {
        return GetAppStoragePath()+ File.separator+"Friend"+FriendID;
    }
    public static String GetAppStoragePath()
    {
        String AppName=ABCApplication.getAppContext().getString(R.string.app_name);
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {

            File podcastFolder = new File(Environment.getExternalStorageDirectory() + File.separator
                    + AppName);
            return podcastFolder.getAbsolutePath();
        } else {
            /* save the folder in internal memory of phone */

            File podcastFolder = new File("/data/data/" + ABCApplication.getAppContext().getPackageName()
                    + AppName);
            return podcastFolder.getAbsolutePath();
        }
    }
    public static void GetAppStorageFolderInitial()
    {
        File f = new File(GetAppStoragePath());
        if(!f.exists()) {
            f.mkdirs();
        }
    }
}
