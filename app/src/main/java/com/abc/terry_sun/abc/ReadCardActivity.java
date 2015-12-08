package com.abc.terry_sun.abc;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by terry_sun on 2015/7/20.
 */
public class ReadCardActivity extends BasicActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CardReaderFragment fragment = new CardReaderFragment();
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }
}
