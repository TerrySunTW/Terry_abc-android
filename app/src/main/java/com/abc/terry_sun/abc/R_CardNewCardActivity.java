package com.abc.terry_sun.abc;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardNewCardActivity extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    Handler messageHandler;
    String LastReadEntityID;
    Thread ProcessThread;
    int GotCardID=0;
    static String EntityCardID;
    final static String TAG="R_CardNewCardActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        HandlerSetting();
        return mScannerView;
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setTouchscreenBlocksFocus(true);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        EntityCardID=rawResult.getText();
        if(rawResult.getBarcodeFormat()== BarcodeFormat.QR_CODE && !rawResult.getText().equals(LastReadEntityID))
        {
            DB_Cards ScannedCard = CardService.getInstance().GetUserOwnCardsByEntityCardID(EntityCardID);
            if (ScannedCard != null) {
                CardService.getInstance().CloseCardDetailDialog();
                CardService.getInstance().ShowCardDetailDialog(ScannedCard.getEntityCardID(),null, MainActivity.GetMainActivityContext());
                return;
            }

            LastReadEntityID = EntityCardID;
            if (ProcessThread == null) {
                ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
                ProcessThread = new Thread(new Runnable() {
                    public void run() {
                        Message msg = new Message();

                        //add new card
                        GotCardID = ServerCommunicationService.getInstance().AddNewCard(EntityCardID);
                        Log.i(TAG,"GotCardID="+String.valueOf(GotCardID));
                        if (GotCardID > 0) {
                            ServerCommunicationService.getInstance().UpdateServerInfo();

                            msg.what = 1;
                        } else {
                            msg.what = 99;
                        }
                        messageHandler.sendMessage(msg);
                    }
                });
                ProcessThread.start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    private void HandlerSetting() {
        messageHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        String NewEntityID=CardService.getInstance().GetCardsByCardID(String.valueOf(GotCardID)).getEntityCardID();
                        CardService.getInstance().ShowCardDetailDialog(NewEntityID,null, MainActivity.GetMainActivityContext());
                        ProcessThread.interrupt();
                        ProcessThread=null;
                        break;
                    case 99:
                        ProcessControlService.AlertMessage(MainActivity.MainActivityContext,"卡片無效!!");
                        ProcessThread.interrupt();
                        ProcessThread=null;
                        LastReadEntityID=null;
                        break;
                }
                super.handleMessage(msg);
                ProcessControlService.CloseProgressDialog();
                mScannerView.resumeCameraPreview(R_CardNewCardActivity.this);
            }
        };
    }

}
