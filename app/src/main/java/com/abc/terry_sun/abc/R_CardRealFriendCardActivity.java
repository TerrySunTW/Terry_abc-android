package com.abc.terry_sun.abc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    int QR_RetryTimes=0;
    static String LastReadQR_Source ="";
    @InjectView(R.id.scanner)
    QRCodeReaderView scanner;
    @InjectView(R.id.ImageView_Scanner)
    ImageView ImageView_Scanner;

    @InjectView(R.id.TextView_Log)
    TextView TextView_Log;

    boolean IsRunning=false;
    static String LastLogMessage;
    static String LastUserCardID;
    static Context _Context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _Context= MainActivity.GetMainActivityContext();
        setContentView(R.layout.activity_r_card_real_friendcard);
        ButterKnife.inject(this);
        HandlerSetting();
        TextView_Log.setText("");
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
                ImageView_Scanner.setImageDrawable(getResources().getDrawable(R.drawable.circle_red));
                switch(msg.what){
                    case 0:
                        //read QR/NFC from EmU
                        new AlertDialog.Builder(_Context)//
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
                                                null,
                                                MainActivity.MainActivityContext);
                                        TextView_Log.setText(TextView_Log.getText() + "\n" + LastLogMessage);
                                    }
                                })//
                                .show();
                        break;
                    case 1:
                        //success //entity card
                        CardService.getInstance().ShowCardDetailDialog(LastReadQR_Source, null ,MainActivity.MainActivityContext);
                        TextView_Log.setText(TextView_Log.getText()+"\n"+LastLogMessage);
                        break;
                    case 99:
                        ProcessControlService.AlertMessage(MainActivity.MainActivityContext, "卡片無效!!");
                        //scanner.getCameraManager().startPreview();
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
                if (QR_string.equals(LastReadQR_Source)) {
                    QR_RetryTimes++;
                    if(QR_RetryTimes>50)
                    {
                        QR_RetryTimes=0;
                        LastReadQR_Source ="";
                    }
                    return;
                }
                Log.i(TAG, "InProcessing");
                QR_RetryTimes=0;
                LastReadQR_Source = QR_string;
                ImageView_Scanner.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");

                //// TODO: 2016/1/21
                if (QR_string.split(",").length == 1) {
                    //Card ID
                    ProcessThread = new Thread(new Runnable() {
                        public void run() {
                            Message msg = new Message();

                            //add new card
                            GotCardID = ServerCommunicationService.getInstance().AddFriendEntityCard(LastReadQR_Source,LastUserCardID);
                            if (GotCardID>0 ) {
                                ServerCommunicationService.getInstance().UpdateServerInfo();
                                LastLogMessage="IP + 1";
                                msg.what = 1;
                            } else {
                                msg.what = 99;
                            }
                            messageHandler.sendMessage(msg);
                        }
                    });
                    ProcessThread.start();


                } else if (QR_string.split(",").length == 2) {
                    LastUserCardID=QR_string.split(",")[1];
                    //Emu card ID
                    ProcessThread = new Thread(new Runnable() {
                        public void run() {
                            Message msg = new Message();
                            //add new card
                            GotCardID = ServerCommunicationService.getInstance().AddFriendNFCCard(LastUserCardID);
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
            }

            @Override
            public void cameraNotFound() {

            }

            @Override
            public void QRCodeNotFoundOnCamImage() {

            }
        });

    }
}
