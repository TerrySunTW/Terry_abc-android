package com.abc.terry_sun.abc;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.facebook.appevents.AppEventsLogger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import eu.livotov.labs.android.camview.CAMView;
import eu.livotov.zxscan.ScannerView;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardNewCardActivity extends BasicActivity {
    String TAG="R_CardNewCardActivity";
    Handler messageHandler;
    Thread ProcessThread;
    static String LastReadEntityID="";
    @InjectView(R.id.scanner)
    ScannerView scanner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_new_card);
        ButterKnife.inject(this);
        HandlerSetting();

        scanner.setScannerViewEventListener(new ScannerView.ScannerViewEventListener() {
            @Override
            public void onScannerReady() {

            }

            @Override
            public void onScannerFailure(int i) {

            }

            public boolean onCodeScanned(final String EntityCardID) {
                //scanner.stopScanner();
                Log.i(TAG, "QRdata=" + EntityCardID);
                if(!EntityCardID.equals(LastReadEntityID)) {
                    Log.i(TAG, "InProcessing");
                    LastReadEntityID = EntityCardID;
                    if (ProcessThread == null) {
                        ProcessControlService.ShowProgressDialog(MainActivity.GetMainActivityContext(), "取得資料處理中...", "");
                        ProcessThread = new Thread(new Runnable() {
                            public void run() {
                                ServerCommunicationService.getInstance().AddNewCard(EntityCardID);
                                ServerCommunicationService.getInstance().GetUserCardInfo();
                                Message msg = new Message();
                                msg.what = 1;
                                messageHandler.sendMessage(msg);
                            }
                        });
                        ProcessThread.start();
                    }
                }
                return true;
            }
        });
        scanner.startScanner();
        //CardService.getInstance().ShowCardDetailDialog("41", MainActivity.GetMainActivityContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stopScanner();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void HandlerSetting() {
        messageHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        CardService.getInstance().CloseCardDetailDialog();
                        CardService.getInstance().ShowCardDetailDialog(LastReadEntityID, MainActivity.GetMainActivityContext());
                        ProcessThread.interrupt();
                        ProcessThread=null;
                        break;
                }
                super.handleMessage(msg);
                ProcessControlService.CloseProgressDialog();
            }
        };
    }
}
