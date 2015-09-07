package com.abc.terry_sun.abc.CustomClass.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;

/**
 * Created by terry_sun on 2015/9/4.
 */
public class AsyncTaskSendNFCCardToServer  extends AsyncTask<Void, Integer, Integer> {
    Context context;
    String NFC_CardID;
    public AsyncTaskSendNFCCardToServer(Context context,String NFC_CardID){
        this.context=context;
        this.NFC_CardID=NFC_CardID;
        ProcessControlService.ShowProgressDialog(context, "作業處理中...", "請稍待...");
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        //send to server
        ServerCommunicationService.getInstance().AddFriendNFCCard(NFC_CardID);
        //download new card
        ServerCommunicationService.getInstance().UpdateServerInfo();
        return null;
    }
    protected void onPostExecute(Integer result) {
        ProcessControlService.CloseProgressDialog();
        CardService.getInstance().ShowCardDetailDialog(NFC_CardID,context);
        //popup card dialog

    }
}
