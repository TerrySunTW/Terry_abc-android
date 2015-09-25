package com.abc.terry_sun.abc;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.facebook.appevents.AppEventsLogger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import eu.livotov.zxscan.ScannerView;

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
    ScannerView scanner;
    @InjectView(R.id.ImageView_Scanner)
    ImageView ImageView_Scanner;
    @InjectView(R.id.ImageView_NFC)
    ImageView ImageView_NFC;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_real_friendcard);
        ButterKnife.inject(this);
        HandlerSetting();

        //NFC
        if (savedInstanceState == null) {
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
            transaction.commit();
        }

        //QR_Code
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
                scanner.stopScanner();
                //same card with previous, do nothing
                if (EntityCardID.equals(LastReadQR_EntityID)) {
                    return true;
                }

                Log.i(TAG, "InProcessing");
                LastReadQR_EntityID = EntityCardID;
                ImageView_Scanner.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
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
}
