package com.abc.terry_sun.abc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Terry on 2015/4/19.
 */
public class LoginActivity extends Activity {

    @InjectView(R.id.editTextUsername)
    protected EditText editTextUsername;

    @InjectView(R.id.editTextPassword)
    protected EditText editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.buttonLogin)
    protected void onButtonClicked() {
        editTextUsername.setText("aaaa");
    }
}
