package com.abc.terry_sun.abc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Terry on 2015/4/19.
 */
public class SettingActivity extends Fragment {
    LoginButton loginButton;
    CallbackManager callbackManager;

    @InjectView(R.id.username)
    protected TextView TextViewUsername;

    Context context;
    private View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.activity_setting,container,false);
        }
        ButterKnife.inject(this, mRootView);
        callbackManager = CallbackManager.Factory.create();
        TextViewUsername.setText(VariableProvider.getInstance().getFacebookUserName());

        loginButton = (LoginButton) mRootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callback registration
                LoginManager.getInstance().logOut();
            }
        });
        return mRootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            AppEventsLogger.activateApp(context);
        } else {
            //相当于Fragment的onPause
            AppEventsLogger.deactivateApp(context);
        }
    }

    @OnClick(R.id.ButtonLink)
    public void GoLinkActivity() {
        ((BaseFragment) getParentFragment()).replaceFragment(new FriendsListActivity(), true);
    }
}
