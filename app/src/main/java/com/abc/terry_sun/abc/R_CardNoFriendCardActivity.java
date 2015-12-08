package com.abc.terry_sun.abc;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;

import butterknife.ButterKnife;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardNoFriendCardActivity extends BasicActivity {
    String TAG="R_CardNoFriendCardActivity";
    static int NewCardID=0;
    FragmentTransaction transaction;
    static CardReaderFragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_no_friendcard);
        ButterKnife.inject(this);
        if (savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            fragment = new CardReaderFragment();
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
                        TabGroup_R_Card.GoPreviousView();
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
        fragment.disableReaderMode();

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public static void  disableReaderMode()
    {
        if(fragment!=null)
        {
            fragment.disableReaderMode();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }
}
