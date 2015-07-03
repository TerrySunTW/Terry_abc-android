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
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ScreenService;

import java.util.ArrayList;
import java.util.List;

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
    List<CategoryInfo> CategoryList;
    List<GroupInfo> GroupList;
    List<CardInfo> CardList;
    List<GalleryItem> GalleryItemList;
    List<GalleryItem> CategoryGalleryItemList;
    String SelectedCategoryID;
    String SelectedGroupID;
    String SelectedCardID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitialParameter();
        setContentView(R.layout.activity_cards);
        ButterKnife.inject(this);

        Log.i("INFO","size.x="+String.valueOf(ScreenService.GetScreenWidth(this)));
        gridView.setColumnWidth((ScreenService.GetScreenWidth(this).x -30)/3);
        GategoryDataSetting();
    }
    void InitialParameter()
    {
        CategoryList = CardService.getInstance().GetAllCategory();
        CategoryGalleryItemList = new ArrayList<GalleryItem>();
        for(CategoryInfo Item:CategoryList)
        {
            CategoryGalleryItemList.add(new GalleryItem(Item.getCategoryID(),Item.getCategoryName(),Item.getCategoryImage()));
        }
    }
    private void GategoryDataSetting() {
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), CategoryGalleryItemList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SelectedCategoryID = CategoryGalleryItemList.get(position).getItemID();
                GroupDataSetting();
                /**
                 Intent intent = new Intent();
                 intent.setClass(CardsActivity.this, CardDetailMainActivity
                 .class);
                 startActivity(intent);**/
            }
        });
    }
    private void GroupDataSetting() {
        GroupList=CardService.getInstance().GetGroupByCategoryID(SelectedCategoryID);
        GalleryItemList = new ArrayList<GalleryItem>();
        for(GroupInfo Item:GroupList)
        {
            GalleryItemList.add(new GalleryItem(Item.getGroupID(),Item.getGroupName(),Item.getGroupImage()));
        }
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), GalleryItemList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SelectedGroupID=GalleryItemList.get(position).getItemID();
            }
        });
    }


    @OnClick(R.id.ButtonCategory)
    protected void onButtonClicked_ButtonCategory() {
        PopupMenu popupMenu = new PopupMenu(this, ButtonCategory);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {
                    //Select all
                    GategoryDataSetting();
                }
                else if (item.getItemId() == 0) {
                    //select Favorite
                }
                else {
                    for (CategoryInfo CategoryInfoItem : CategoryList) {
                        if (CategoryInfoItem.getCategoryName().equals(item.getTitle().toString())) {
                            SelectedCategoryID = CategoryInfoItem.getCategoryID();
                            GroupDataSetting();
                        }
                    }
                }
                return false;
            }
        });
        popupMenu.getMenu().add(0,0,0,"All");
        popupMenu.getMenu().add(0,1,0,"Favorite");
        for(int i=0; i<CategoryList.size(); i++)
        {
            popupMenu.getMenu().add(CategoryList.get(i).getCategoryName());
        }
        popupMenu.show();
    }
    @OnClick(R.id.ButtonGroup)
    protected void onButtonClicked_ButtonGroup() {

    }
    @OnClick(R.id.ButtonRepresentative)
    protected void onButtonClicked_ButtonRepresentative() {

    }
}
