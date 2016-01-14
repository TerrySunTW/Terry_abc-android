package com.abc.terry_sun.abc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eu.livotov.labs.android.camview.ScannerLiveView;

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
    ScannerLiveView scanner;
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
        //if(scanner.isAttachedToWindow()) {
            scanner.stopScanner();
        //}
        super.onPause();

    }

    @Override
    protected void onResume() {
        Log.i(TAG,"onResume");
        IsRunning=true;
        LastReadEntityID=null;
        super.onResume();
        scanner.startScanner();
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
        scanner.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Log.i(TAG,"onScannerStarted");
                if(!IsRunning)
                {
                    Log.i(TAG,"onScannerStarted-stop");
                    scanner.stopScanner();
                }
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                Log.i(TAG,"onScannerStopped");
            }

            @Override
            public void onScannerError(Throwable err) {
                Log.i(TAG, "onScannerError");
                Log.e(TAG, err.getMessage());
            }

            public void onCodeScanned(final String EntityCardID) {
                //scanner.stopScanner();
                Log.i(TAG, "QRdata=" + EntityCardID);
                scanner.stopScanner();
                //same card with previous
                if (EntityCardID.equals(LastReadEntityID)) {
                    scanner.startScanner();
                    return;
                }

                //local card
                DB_Cards ScannedCard = CardService.getInstance().GetUserOwnCardsByEntityCardID(EntityCardID);
                if (ScannedCard != null) {
                    CardService.getInstance().CloseCardDetailDialog();
                    CardService.getInstance().ShowCardDetailDialog(ScannedCard.getEntityCardID(), MainActivity.GetMainActivityContext());
                    scanner.startScanner();
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
        });
    }
}
