package com.abc.terry_sun.abc.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class ProcessControlService {
    static private ProgressDialog MyProgressDialog;
    public static void ShowProgressDialog(Context _Context,String Title,String Message)
    {
        CloseProgressDialog();
        if(!((Activity) _Context).isFinishing()) {
            try
            {
                MyProgressDialog = ProgressDialog.show(_Context, Title, Message, true);
            }
            catch (Exception ex)
            {
            }
        }
    }
    public static void CloseProgressDialog()
    {
        if(MyProgressDialog!=null)
        {
            try
            {
                MyProgressDialog.dismiss();
            }
            catch (Exception ex)
            {
            }
            MyProgressDialog=null;
        }
    }
    public static void QuitProcess(final Context _Context)
    {
        new AlertDialog.Builder(_Context)//
                .setMessage("是否離開")//
                .setPositiveButton("確定",//
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent stopServiceIntent = new Intent(_Context, _Context.getClass());
                                _Context.stopService(stopServiceIntent);
                                System.exit(0);//關閉程式語法
                                // 或使用Process.killProcess(Process.myPid());
                            }
                        })//
                .setNeutralButton("不離開", null)//
                .show();
    }
    public static void SystemErrorQuitProcess(Context _Context,String Message)
    {
        new AlertDialog.Builder(_Context)//
                .setMessage(Message)//
                .setPositiveButton("確定",//
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                System.exit(0);//關閉程式語法
                                // 或使用Process.killProcess(Process.myPid());
                            }
                        })//
                .show();
    }
    public static void AlertMessage(Context _Context,String Message)
    {
        AlertMessage(_Context, Message,"",null);
    }
    public static void AlertMessage(Context _Context,String Message,DialogInterface.OnClickListener ButtonOnClickListener)
    {
        AlertMessage(_Context, Message,"",ButtonOnClickListener);
    }
    public static void AlertMessage(Context _Context,String Title,String Message)
    {
        AlertMessage(_Context, Title, Message, null);
    }
    public static void AlertMessage(Context _Context,String Title,String Message,DialogInterface.OnClickListener ButtonOnClickListener)
    {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(_Context);
        MyAlertDialog.setTitle(Title);
        if(Message.length()>0) {
            MyAlertDialog.setMessage(Message);
        }
        MyAlertDialog.setNeutralButton("確認", ButtonOnClickListener);
        MyAlertDialog.show();
    }
}
