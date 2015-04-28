package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.abc.terry_sun.abc.Provider.VariableProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by Terry on 2015/4/26.
 */
public class Test_AndroidService  extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androidservicetest);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.buttonStartService)
    protected void onButtonClicked_Start_Service() {
        Log.e("Event", "onButtonClicked_Start_Service");
        VariableProvider.getInstance().setIsRuningServiceThread(true);
        Intent startServiceIntent = new Intent(this, MyService.class);
        startService(startServiceIntent);
    }

    @OnClick(R.id.buttonStopService)
    protected void onButtonClicked_Stop_Service() {
        Log.e("Event", "onButtonClicked_Stop_Service");
        VariableProvider.getInstance().setIsRuningServiceThread(false);
        Intent stopServiceIntent = new Intent(this, MyService.class);
        stopService(stopServiceIntent);

    }
}
