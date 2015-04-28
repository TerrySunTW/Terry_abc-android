package com.abc.terry_sun.abc;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.abc.terry_sun.abc.Provider.VariableProvider;

/**
 * Created by terry_sun on 2015/4/28.
 */
public class MyService extends Service {
    private Thread BackgroundThread;
    private MyBinder mBinder = new MyBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = new Notification(R.drawable.ic_launcher,
                "服務運行中...", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, this.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(this, "這是通知的標題", "這是通知的內容", pendingIntent);
        startForeground(1, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Event", "onStartCommand");
        BackgroundThread=new Thread(new Runnable() {
            @Override
            public void run() {
                // 開始執行Service
                while (VariableProvider.getInstance().isIsRuningServiceThread())
                {
                    Log.e("Event", "Running StartCommand!!");
                    try {
                        Thread.sleep(1000);
                    }
                    catch (Exception e)
                    {

                    }
                }
            }
        });
        BackgroundThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.e("Event", "onDestroy()");
        super.onDestroy();
        BackgroundThread.interrupt();
        BackgroundThread=null;
    }
}
class MyBinder extends Binder {
    public void startDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //執行Service任務
            }
        }).start();
    }
}
