package com.abc.terry_sun.abc;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
/**
 * Created by Terry on 2015/4/26.
 */
public class Test_SampleTestActivity  extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androidservicetest);
        ButterKnife.bind(this);
        //SocketService.IsCorrectStringWithCRC("@022OTAwMXxTRU5ERlVU2E");
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
