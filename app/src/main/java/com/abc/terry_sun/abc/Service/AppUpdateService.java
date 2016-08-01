package com.abc.terry_sun.abc.Service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.abc.terry_sun.abc.Provider.HttpURL_Provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by terry_sun on 2015/10/5.
 */
public class AppUpdateService {
    private static final AppUpdateService _AppUpdateService = new AppUpdateService();
    public static AppUpdateService getInstance() {
        return _AppUpdateService;
    }
    static final String TAG="AppUpdateService";
    Context context;
    public void DownloadAPK(Context context){
        this.context=context;
        ProcessControlService.ShowProgressDialog(context, "新版APP", "APP更新中...");
        Thread DownloadAPKThread=new Thread(new Runnable() {
            public void run() {
                try {

                    URL url = new URL(HttpURL_Provider.AppURL);

                    FileOutputStream fileOutput = new FileOutputStream(GetApkFile());
                    InputStream inputStream = url.openStream();

                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;

                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                    }
                    fileOutput.close();
                    InstallAPK();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        DownloadAPKThread.start();
    }
    private void InstallAPK(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(GetApkFile());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public void RemoveAPK(){
        File ApkFile=GetApkFile();
        if(ApkFile.exists()) {
            GetApkFile().delete();
        }
    }
    private File GetApkFile()
    {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, "ABC.apk");
    }
}

