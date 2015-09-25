package com.abc.terry_sun.abc;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
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
public class R_CardNoFriendCardActivity extends BasicActivity {
    String TAG="R_CardNoFriendCardActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_no_friendcard);
        ButterKnife.inject(this);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CardReaderFragment fragment = new CardReaderFragment();
            fragment.SetAsyncTaskProcessingInterface(new AsyncTaskProcessingInterface() {
                @Override
                public void DoProcessing() {
                    //send to server
                    ServerCommunicationService.getInstance().AddFriendNFCCard(VariableProvider.getInstance().getLastNFCKey());
                    //download new card
                    ServerCommunicationService.getInstance().UpdateServerInfo();
                }
            });
            transaction.replace(R.id.fragmentlayout_readcard, fragment);
            transaction.commit();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
