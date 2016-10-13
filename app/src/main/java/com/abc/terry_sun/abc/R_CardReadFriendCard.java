package com.abc.terry_sun.abc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardReadFriendCard extends Fragment implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    ImageView ImageView_Scanner;
    TextView TextView_Log;
    Handler messageHandler;
    String LastReadEntityID;
    String LastReadUserCardID;
    Thread ProcessThread;
    static String LastLogMessage;
    int GotCardID=0;
    static String EntityCardID;
    final static String TAG="R_CardReadFriendCard";
    private View mRootView;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.activity_r_card_zbar_layout,container,false);
        }
        context=getActivity();
        ViewGroup contentFrame = (ViewGroup) mRootView.findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(context);
        contentFrame.addView(mScannerView);
        ImageView_Scanner=(ImageView)mRootView.findViewById(R.id.ImageView_Scanner);
        TextView_Log=(TextView)mRootView.findViewById(R.id.TextView_Log);
        TextView_Log.setText("");
        HandlerSetting();
        return mRootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result){
        final String ReadCardID=result.getText();
        //scan virtual QR-Code
        Log.i(TAG, "handleResult ReadCardID="+ReadCardID);
        Log.i(TAG, "handleResult LastReadEntityID="+LastReadEntityID);
        if(ReadCardID.equals(LastReadEntityID))
        {
            return;
        }
        Log.i(TAG, "ReadCardID="+ReadCardID);
        Log.i(TAG, "ReadCardID.split="+String.valueOf(ReadCardID.split(",").length));
        if(ReadCardID.split(",").length==1)
        {
            Log.i(TAG, "RealCard Processing");
            LastReadEntityID=ReadCardID;
            ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
            //Emu card ID
            ProcessThread = new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();

                    //add new card
                    GotCardID = ServerCommunicationService.getInstance().AddFriendEntityCard(ReadCardID);
                    Log.i(TAG, "FacebookID="+VariableProvider.getInstance().getFacebookID());
                    Log.i(TAG, "ReadCardID="+ReadCardID);
                    Log.i(TAG, "GotCardID="+GotCardID);
                    if (GotCardID>0) {
                        ServerCommunicationService.getInstance().UpdateServerInfo();
                        EntityCardID=String.valueOf(GotCardID);
                        msg.what = 1;
                    } else {
                        msg.what = 99;
                    }
                    messageHandler.sendMessage(msg);
                }
            });
            ProcessThread.start();
        }
        else
        {
            //[Time , UserCardID]
            LastReadEntityID=ReadCardID;
            Log.i(TAG, "Virtual Card Processing");
            ImageView_Scanner.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
            LastReadUserCardID = ReadCardID.split(",")[1];;
            ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
            //Emu card ID
            ProcessThread = new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    //add new card
                    GotCardID = ServerCommunicationService.getInstance().AddFriendVirtualCard(LastReadUserCardID);
                    Log.i(TAG, "LastReadUserCardID="+LastReadUserCardID);
                    Log.i(TAG, "FacebookID="+VariableProvider.getInstance().getFacebookID());
                    Log.i(TAG, "GotCardID="+GotCardID);
                    if (GotCardID>0) {
                        ServerCommunicationService.getInstance().UpdateServerInfo();
                        msg.what = 0 ;
                    } else {
                        msg.what = 99;
                    }
                    messageHandler.sendMessage(msg);
                }
            });
            ProcessThread.start();
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
                Log.i(TAG, "msg.what="+String.valueOf(msg.what));
                switch(msg.what){
                    case 0:
                        //read virtual card
                        CardService.getInstance().ShowCardDetailDialog(
                                CardService.getInstance().GetEntityCardIDByCardID(String.valueOf(GotCardID)),
                                "successfully read a virtual card.",
                                MainActivity.MainActivityContext);
                        break;
                    case 1:
                        //success //entity card
                        CardService.getInstance().ShowCardDetailDialog(
                                CardService.getInstance().GetEntityCardIDByCardID(String.valueOf(EntityCardID)),
                                "successfully read a real card.",
                                MainActivity.MainActivityContext);
                        TextView_Log.setText("You can read virtual card to earn DP/IP.");
                        break;
                    case 99:
                        ProcessControlService.AlertMessage(MainActivity.MainActivityContext, "卡片無效!!");
                        break;
                }
                super.handleMessage(msg);
                ProcessControlService.CloseProgressDialog();
            }
        };
    }
}
