package com.abc.terry_sun.abc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import com.abc.terry_sun.abc.Provider.BusProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Terry on 2015/4/19.
 */
public class Test_EventBusActivity extends Activity {

    @BindView(R.id.editTextUsername)protected EditText editTextUsername;

    @BindView(R.id.editTextPassword)protected EditText editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbustest);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);//注册

    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);//注销
    }

    @Subscribe   //订阅事件DataChangedEvent
    public void sayGoodOnEvent(DataChangedEvent event){
        Log.e("event", "good");
    }

    @Subscribe  //订阅事件
    public void sayBadOnEvent(DataChangedEvent event){
        Log.e("event", "bad");
    }

    @Produce    //产生事件
    public DataChangedEvent produceDataChangedEvent(){
        return new DataChangedEvent("this is changed String");
    }
    @OnClick(R.id.buttonLogin)
    protected void onButtonClicked() {
        //editTextUsername.setText("aaaa");
        BusProvider.getInstance().post(produceDataChangedEvent());
    }
}
