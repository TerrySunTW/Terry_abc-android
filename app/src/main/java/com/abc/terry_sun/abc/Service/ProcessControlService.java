package com.abc.terry_sun.abc.Service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class ProcessControlService {
    public static void QuitProcess(Context _Context)
    {
        new AlertDialog.Builder(_Context)//
                .setMessage("是否離開")//
                .setPositiveButton("確定",//
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                System.exit(0);//關閉程式語法
                                // 或使用Process.killProcess(Process.myPid());
                            }
                        })//
                .setNeutralButton("不離開", null)//
                .show();
    }
}
