package com.abc.terry_sun.abc.CustomClass.AsyncTask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by terry_sun on 2015/6/26.
 */
public class AsyncTaskHttpRequest  extends AsyncTask<Void, Integer, Integer> {

    private Context myCtx;
    ProgressDialog Tempdialog;
    Boolean CheckResulteIsPass=null;
    String ErrorMessage="";
    AsyncTaskProcessingInterface _AsyncTaskProcessingInterface;
    public AsyncTaskHttpRequest(Context ctx, AsyncTaskProcessingInterface asyncTaskProcessingInterfac){
        this.myCtx = ctx;
        this._AsyncTaskProcessingInterface=asyncTaskProcessingInterfac;
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        CheckResulteIsPass=true;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        //Log.i("Event", "AsyncTaskPurchaseStock.doInBackground.CheckResulteIsPass=" + String.valueOf(CheckResulteIsPass));
        while (CheckResulteIsPass==null)
        {
            try {
                Thread.sleep(100);
            }
            catch(Exception e) {}
        }
        _AsyncTaskProcessingInterface.DoProcessing();
        return null;
    }
    protected void onPostExecute(Integer result) {
        //Log.i("Event", "AsyncTaskPurchaseStock.onPostExecute.CheckResulteIsPass="+String.valueOf(CheckResulteIsPass));
        if(CheckResulteIsPass)
        {
            CheckResulteIsPass=null;
        }
    }

}
