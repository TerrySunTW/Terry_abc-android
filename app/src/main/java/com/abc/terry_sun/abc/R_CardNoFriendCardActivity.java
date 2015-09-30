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
public class R_CardNoFriendCardActivity extends BasicActivity {
    String TAG="R_CardNoFriendCardActivity";
    static int NewCardID=0;
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
                    NewCardID=ServerCommunicationService.getInstance().AddFriendNFCCard(VariableProvider.getInstance().getLastNFCKey());
                    //download new card
                    ServerCommunicationService.getInstance().UpdateServerInfo();
                }
            });
            fragment.SetAsyncTaskPostProcessing(new AsyncTaskPostProcessingInterface() {
                @Override
                public void DoProcessing() {
                    DB_Cards _DB_Cards=CardService.getInstance().GetCardsByCardID(String.valueOf(NewCardID));
                    if(_DB_Cards!=null)
                    {
                        CardService.getInstance().ShowCardDetailDialog(_DB_Cards.getEntityCardID(), MainActivity.GetMainActivityContext());
                    }
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
