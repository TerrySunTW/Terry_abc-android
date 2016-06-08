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
        return ABCApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }
    public static void GetAppStorageFolderInitial()
    {
        File f = new File(GetAppStoragePath());
        if(!f.exists()) {
            f.mkdirs();
        }
    }
}
