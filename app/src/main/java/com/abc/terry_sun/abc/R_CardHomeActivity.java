package com.abc.terry_sun.abc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class R_CardHomeActivity extends BaseFragment {
    String TAG="R_CardHomeActivity";


    Context context;
    private View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.activity_r_cardhome,container,false);
        }
        context=getActivity();
        ButterKnife.inject(this, mRootView);
        return mRootView;
    }

    @OnClick(R.id.ButtonNewCard)
    protected void onButtonClicked_ButtonNewCard() {
        Log.i(TAG,"onButtonClicked_ButtonNewCard");
        ((BaseFragment) getParentFragment()).replaceFragment(new R_CardNewCardActivity(), true);
    }
    @OnClick(R.id.ButtonFriendCard)
    protected void onButtonClicked_ButtonFriendCard() {
        Log.i(TAG,"onButtonClicked_ButtonFriendCard");
        ((BaseFragment) getParentFragment()).replaceFragment(new R_CardRealFriendCardActivity(), true);
    }

}
