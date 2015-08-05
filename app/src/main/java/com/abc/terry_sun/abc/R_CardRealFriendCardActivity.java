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

import com.abc.terry_sun.abc.Models.GalleryItem;
import com.facebook.appevents.AppEventsLogger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardRealFriendCardActivity extends BasicActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_card_real_friendcard);
        ButterKnife.inject(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.ButtonHasRealCard)
    protected void onButtonClicked_ButtonHasRealCard() {
        Intent _Intent = new Intent(this,R_CardNewCardActivity.class);//跳頁
        TabGroup_R_Card.ChangeActivity( _Intent, true);
    }
    @OnClick(R.id.ButtonNoRealCard)
    protected void onButtonClicked_ButtonNoRealCard() {
        Intent _Intent = new Intent(this,R_CardFriendCardActivity.class);//跳頁
        TabGroup_R_Card.ChangeActivity( _Intent, true);
    }
}
