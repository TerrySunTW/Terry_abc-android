package com.abc.terry_sun.abc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.abc.terry_sun.abc.Entities.TestDB_Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Terry on 2015/4/19.
 */
public class Test_DBActivity extends Activity {

    @BindView(R.id.editTextUsername)protected EditText editTextUsername;

    @BindView(R.id.editTextPassword)protected EditText editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonLogin)
    protected void onButtonClicked() {
        //editTextUsername.setText("aaaa");
        TestDB_Book book = new TestDB_Book("test","adfa");
        book.save();
        //list items
        List<TestDB_Book> aaa=TestDB_Book.listAll(TestDB_Book.class);
        Log.e("ERROR", String.valueOf(aaa.size()));
    }
}
