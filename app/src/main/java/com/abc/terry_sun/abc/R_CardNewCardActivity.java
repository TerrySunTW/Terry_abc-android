package com.abc.terry_sun.abc;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardNewCardActivity extends BasicActivity {
    String TAG="R_CardNewCardActivity";
    Handler messageHandler;
    Thread ProcessThread;
    int GotCardID=0;
    static String LastReadEntityID="";
    @InjectView(R.id.scanner)
    QRCodeReaderView scanner;
    boolean IsRunning=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_new_card);
        ButterKnife.inject(this);
        HandlerSetting();
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
        LastReadEntityID=null;
        super.onResume();
        scanner.getCameraManager().startPreview();
    }
    private void HandlerSetting() {
        messageHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        String NewEntityID=CardService.getInstance().GetCardsByCardID(String.valueOf(GotCardID)).getEntityCardID();
                        CardService.getInstance().ShowCardDetailDialog(NewEntityID, MainActivity.GetMainActivityContext());
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
            }
        };
    }
    private void QR_Setting()
    {
        scanner.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(final String EntityCardID, PointF[] points) {
                //scanner.stopScanner();
                Log.i(TAG, "QRdata=" + EntityCardID);
                scanner.getCameraManager().stopPreview();
                //same card with previous
                if (EntityCardID.equals(LastReadEntityID)) {
                    scanner.getCameraManager().startPreview();
                    return;
                }

                //local card
                DB_Cards ScannedCard = CardService.getInstance().GetUserOwnCardsByEntityCardID(EntityCardID);
                if (ScannedCard != null) {
                    CardService.getInstance().CloseCardDetailDialog();
                    CardService.getInstance().ShowCardDetailDialog(ScannedCard.getEntityCardID(), MainActivity.GetMainActivityContext());
                    scanner.getCameraManager().startPreview();
                    return;
                }

                Log.i(TAG, "InProcessing");
                LastReadEntityID = EntityCardID;
                if (ProcessThread == null) {
                    ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
                    ProcessThread = new Thread(new Runnable() {
                        public void run() {
                            Message msg = new Message();

                            //add new card
                            GotCardID = ServerCommunicationService.getInstance().AddNewCard(EntityCardID);
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

            @Override
            public void cameraNotFound() {

            }

            @Override
            public void QRCodeNotFoundOnCamImage() {

            }
        });
    }
}
