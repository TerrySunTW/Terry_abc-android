package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.terry_sun.abc.CustomClass.Adapter.AdapterCardsImage;
import com.abc.terry_sun.abc.Service.ScreenService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class CardsActivity extends Activity {

    @InjectView(R.id.gridView1)
    GridView gridView;
    @InjectView(R.id.ButtonCategory)
    Button ButtonCategory;
    static final String[] Categories = new String[]{
            "NBA", "MLB", "MLB", "MLB"};
    static final String[] Groups = new String[]{
            "NBA", "MLB", "MLB", "MLB"};
    @OnClick(R.id.ButtonCategory)
    protected void onButtonClicked_ButtonCategory() {
        PopupMenu popupMenu = new PopupMenu(this, ButtonCategory);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String SelectText=item.getTitle().toString();
                return false;
            }
        });
        for(int i=0; i<Categories.length; i++)
        {
            popupMenu.getMenu().add(Categories[i]);
        }
        popupMenu.show();
    }
    @OnClick(R.id.ButtonGroup)
    protected void onButtonClicked_ButtonGroup() {

    }
    @OnClick(R.id.ButtonRepresentative)
    protected void onButtonClicked_ButtonRepresentative() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        ButterKnife.inject(this);

        Log.i("INFO","size.x="+String.valueOf(ScreenService.GetScreenWidth(this)));
        gridView.setColumnWidth((ScreenService.GetScreenWidth(this).x -30)/3);
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), Categories));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CardsActivity.this, CardDetailMainActivity
                        .class);
                startActivity(intent);
            }
        });

    }
}
