package com.abc.terry_sun.abc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
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

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;


import butterknife.ButterKnife;
import butterknife.InjectView;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardReadFriendCard extends Fragment implements ZBarScannerView.ResultHandler{
    private ZBarScannerView mScannerView;
    ImageView ImageView_Scanner;
    TextView TextView_Log;
    Handler messageHandler;
    String LastReadEntityID;
    String LastReadUserCardID;
    Thread ProcessThread;
    private Boolean HasReadRealCard=false;
    static String LastLogMessage;
    int GotCardID=0;
    static String EntityCardID;
    final static String TAG="R_CardReadFriendCard";
    private View mRootView;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HasReadRealCard=false;
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.activity_r_card_zbar_layout,container,false);
        }
        context=getActivity();
        ViewGroup contentFrame = (ViewGroup) mRootView.findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(context);
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
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        final String ReadCardID=result.getContents();
        //scan virtual QR-Code
        if(ReadCardID.equals(LastReadEntityID))
        {
            return;
        }

        if(!HasReadRealCard )
        {

            //[Time , UserCardID]
            LastReadEntityID=ReadCardID;
            Log.i(TAG, "InProcessing");
            ImageView_Scanner.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
            LastReadUserCardID = ReadCardID.split(",")[1];;
            ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
            //Emu card ID
            ProcessThread = new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    //add new card
                    GotCardID = ServerCommunicationService.getInstance().AddFriendNFCCard(LastReadUserCardID);
                    if (GotCardID>0) {
                        ServerCommunicationService.getInstance().UpdateServerInfo();
                        LastLogMessage="DP + 1";
                        msg.what = 0 ;
                    } else {
                        msg.what = 99;
                    }
                    messageHandler.sendMessage(msg);
                }
            });
            ProcessThread.start();
        }
        //scan real card Code
        else
        {
            LastReadEntityID=ReadCardID;
            ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
            //Emu card ID
            ProcessThread = new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    //add new card
                    GotCardID = ServerCommunicationService.getInstance().AddFriendEntityCard(LastReadUserCardID,ReadCardID);
                    if (GotCardID>0) {
                        LastLogMessage="IP + 1";
                        EntityCardID=String.valueOf(GotCardID);
                        msg.what = 1 ;
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
                switch(msg.what){
                    case 0:
                        HasReadRealCard=true;
                        //read QR/NFC from EmU
                        new AlertDialog.Builder(MainActivity.GetMainActivityContext())//
                                .setMessage("Wanna read real card?")//
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                VariableProvider.getInstance().setLastNFCKey(String.valueOf(GotCardID));
                                            }
                                        })//
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        CardService.getInstance().ShowCardDetailDialog(
                                                CardService.getInstance().GetEntityCardIDByCardID(String.valueOf(GotCardID)),
                                                "successfully read a virtual card.",
                                                MainActivity.MainActivityContext);
                                        TextView_Log.setText(TextView_Log.getText() + "\n" + LastLogMessage);
                                    }
                                })//
                                .show();
                        break;
                    case 1:
                        //success //entity card
                        CardService.getInstance().ShowCardDetailDialog(
                                CardService.getInstance().GetEntityCardIDByCardID(String.valueOf(EntityCardID)),
                                "successfully read a real card.",
                                MainActivity.MainActivityContext);
                        TextView_Log.setText(TextView_Log.getText()+"\n"+LastLogMessage);
                        break;
                    case 99:
                        ProcessControlService.AlertMessage(MainActivity.MainActivityContext, "卡片無效!!");
                        break;
                }
                super.handleMessage(msg);
                ProcessControlService.CloseProgressDialog();
                mScannerView.resumeCameraPreview(R_CardReadFriendCard.this);
            }
        };
    }

}
