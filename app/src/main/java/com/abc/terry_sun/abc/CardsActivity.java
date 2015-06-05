package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.terry_sun.abc.CustomClass.Adapter.AdapterCardsImage;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class CardsActivity extends Activity {

    GridView gridView;

    static final String[] MOBILE_OS = new String[]{
            "Android", "iOS", "Windows", "Blackberry"};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        gridView = (GridView) findViewById(R.id.gridView1);

        gridView.setAdapter(new AdapterCardsImage(this, MOBILE_OS));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent();
                intent.setClass(CardsActivity.this, CardDetailMainActivity
                        .class);
                startActivity(intent);
               /**
                Toast.makeText(
                        getApplicationContext(),
                        ((TextView) v.findViewById(R.id.grid_item_label)).getText(),
                        Toast.LENGTH_SHORT
                        //CardDetailMainActivity
                ).show();**/

            }
        });

    }
}
