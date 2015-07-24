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
    AsyncTaskProcessingInterface _AsyncTaskProcessingInterface;
    public AsyncTaskHttpRequest(AsyncTaskProcessingInterface asyncTaskProcessingInterfac){
        this._AsyncTaskProcessingInterface=asyncTaskProcessingInterfac;
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        _AsyncTaskProcessingInterface.DoProcessing();
        return null;
    }
    protected void onPostExecute(Integer result) {
        //Log.i("Event", "AsyncTaskPurchaseStock.onPostExecute.CheckResulteIsPass="+String.valueOf(CheckResulteIsPass));

    }

}
