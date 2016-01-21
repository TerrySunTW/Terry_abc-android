package com.abc.terry_sun.abc;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardRealFriendCardActivity extends BasicActivity {
    String TAG="R_CardRealFriendCardActivity";
    Handler messageHandler;
    Thread ProcessThread;
    int GotCardID=0;
    static String LastReadQR_EntityID="";
    @InjectView(R.id.scanner)
    QRCodeReaderView scanner;
    @InjectView(R.id.ImageView_Scanner)
    ImageView ImageView_Scanner;

    @InjectView(R.id.ImageView_Scanner2)
    ImageView ImageView_Scanner2;


    @InjectView(R.id.ImageView_NFC)
    ImageView ImageView_NFC;
    @InjectView(R.id.LinearLayout_QR)
    LinearLayout LinearLayout_QR;
    @InjectView(R.id.LinearLayout_NFC)
    LinearLayout LinearLayout_NFC;
    boolean IsRunning=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_real_friendcard);
        ButterKnife.inject(this);
        HandlerSetting();

        //NFC
        if (savedInstanceState == null) {
            //NFC
            if(VariableProvider.getInstance().getNFC_Enable()) {
                LinearLayout_NFC.setVisibility(View.VISIBLE);
                LinearLayout_QR.setVisibility(View.GONE);
                NFC_Setting();
            }
            else
            {
                LinearLayout_QR.setVisibility(View.VISIBLE);
                LinearLayout_NFC.setVisibility(View.GONE);
            }
        }

        //QR_Code
        QR_Setting();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        IsRunning=false;
        scanner.getCameraManager().stopPreview();
        super.onPause();

    }
    @Override
    protected void onResume() {
        Log.i(TAG,"onResume");
        IsRunning=true;
        super.onResume();
        scanner.getCameraManager().startPreview();
    }
    private void HandlerSetting() {
        messageHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        //success
                        CardService.getInstance().ShowCardDetailDialog(LastReadQR_EntityID, MainActivity.MainActivityContext);
                        ProcessThread.interrupt();
                        ProcessThread=null;
                        break;
                    case 99:
                        ProcessControlService.AlertMessage(MainActivity.MainActivityContext,"卡片無效!!");
                        ProcessThread.interrupt();
                        ProcessThread=null;
                        break;
                }
                super.handleMessage(msg);
                ProcessControlService.CloseProgressDialog();
            }
        };
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }
    private void QR_Setting()
    {
        scanner.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String QR_string, PointF[] points) {
                Log.i(TAG, "QR_string=" + QR_string);
                if (QR_string.equals(LastReadQR_EntityID)) {
                    return;
                }
                Log.i(TAG, "InProcessing");
                LastReadQR_EntityID = QR_string;

                if (QR_string.split(",").length == 1) {
                    //Action1
                    ImageView_Scanner.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                } else {
                    ImageView_Scanner2.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                }
            }

            @Override
            public void cameraNotFound() {

            }

            @Override
            public void QRCodeNotFoundOnCamImage() {

            }
        });

    }
    private void NFC_Setting()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CardReaderFragment fragment = new CardReaderFragment();
        fragment.SetAsyncTaskPostProcessing(
                new AsyncTaskPostProcessingInterface() {
                    @Override
                    public void DoProcessing() {
                        ImageView_NFC.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                        if (ProcessThread == null && VariableProvider.getInstance().CheckLastNFCKeyIsNotNull()) {
                            ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
                            ProcessThread = new Thread(new Runnable() {
                                public void run() {
                                    Message msg = new Message();

                                    //add new card
                                    GotCardID = ServerCommunicationService.getInstance().AddFriendEntityCard(LastReadQR_EntityID);
                                    if (GotCardID>0) {
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
                });
        transaction.replace(R.id.fragmentlayout_readcard, fragment);
        transaction.commitAllowingStateLoss();
    }
}
