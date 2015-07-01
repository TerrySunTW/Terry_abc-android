package com.abc.terry_sun.abc;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.terry_sun.abc.CustomClass.Adapter.AdapterCardsImage;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class CardDetailMainActivity extends TabActivity {
    TabHost tabHost;
    /** Called when the activity is first created. */


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail_main);
        tabHost = getTabHost();
        setTabs();
    }
    private void setTabs()
    {

        addTab("Info", R.drawable.tab_search, CardDetailInfoActivity.class);
        addTab("Bonus", R.drawable.tab_home, CardDetailBonusActivity.class);
        addTab("Fake", R.drawable.tab_search, CardDetailEmulateActivity.class);
        addTab("Settings", R.drawable.tab_search, CardDetailSettingsActivity.class);
        addTab("Emulate", R.drawable.tab_search, CardDetailEmulateActivity.class);
    }
    private void addTab(String labelId, int drawableId, Class<?> c)
    {
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.cards_tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }
    public void openCameraActivity(View b)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
