package com.abc.terry_sun.abc;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.abc.terry_sun.abc.Models.GalleryItem;
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


    @InjectView(R.id.scanner)
    ScannerView scanner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_new_card);
        ButterKnife.inject(this);

        scanner.setScannerViewEventListener(new ScannerView.ScannerViewEventListener() {
            @Override
            public void onScannerReady() {

            }

            @Override
            public void onScannerFailure(int i) {

            }

            public boolean onCodeScanned(final String EntityCardID) {
                scanner.stopScanner();
                Log.i(TAG, "QRdata=" + EntityCardID);
                ServerCommunicationService.getInstance().AddNewCard(EntityCardID);
                ServerCommunicationService.getInstance().GetUserCardInfo();
                return true;
            }
        });
        scanner.startScanner();
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
}
