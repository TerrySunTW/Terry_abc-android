package com.abc.terry_sun.abc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.abc.terry_sun.abc.Entities.TestDB_Book;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by terry_sun on 2015/4/29.
 */
public class Test_GalleryActivity extends Activity {

    @InjectView(R.id.editTextUsername)
    protected EditText editTextUsername;

    @InjectView(R.id.editTextPassword)
    protected EditText editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
        ButterKnife.inject(this);
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
