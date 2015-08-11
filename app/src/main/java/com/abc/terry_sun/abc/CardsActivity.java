package com.abc.terry_sun.abc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.abc.terry_sun.abc.CustomClass.Adapter.AdapterCardsImage;
import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.ScreenService;
import com.abc.terry_sun.abc.Service.StorageService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class CardsActivity extends BasicActivity {

    @InjectView(R.id.gridView1)
    GridView gridView;
    @InjectView(R.id.ButtonCategory)
    Button ButtonCategory;
    @InjectView(R.id.ButtonGroup)
    Button ButtonGroup;
    @InjectView(R.id.ButtonRepresentative)
    Button ButtonRepresentative;

    List<CategoryInfo> CategoryList;
    List<GroupInfo> GroupList;
    List<RepresentativeInfo> RepresentativeList;
    List<CardInfo> CardList;

    List<GalleryItem> GalleryItemList;

    //MenuItem
    List<GalleryItem> CategoryGalleryItemList;
    List<GalleryItem> GroupGalleryItemList;
    List<GalleryItem> RepresentativeGalleryItemList;
    List<GalleryItem> CardGalleryItemList;

    String SelectedCategoryID;
    String SelectedGroupID;
    String SelectedRepresentativeID;
    String SelectedEntityCardID;

    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this.getParent();
        setContentView(R.layout.activity_cards);
        ButterKnife.inject(this);

        Log.i("INFO", "size.x=" + String.valueOf(ScreenService.GetScreenWidth(this)));
        gridView.setColumnWidth((ScreenService.GetScreenWidth(this).x - 30) / 3);

    }
    @Override
    protected void onResume() {
        super.onResume();
        InitialParameter();
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
                ButtonCategory.setText(CategoryGalleryItemList.get(position).getTitle());
                SelectedCategoryID = CategoryGalleryItemList.get(position).getItemID();
                Log.i("Info", "SelectedCategoryID" + SelectedCategoryID);
                GroupDataSetting();
            }
        });
    }

    private void GroupDataSetting() {
        GroupList=CardService.getInstance().GetGroupByCategoryID(SelectedCategoryID);
        GroupGalleryItemList = new ArrayList<GalleryItem>();
        for(GroupInfo Item:GroupList)
        {
            GroupGalleryItemList.add(new GalleryItem(Item.getGroupID(),Item.getGroupName(),Item.getGroupImage()));
        }
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), GroupGalleryItemList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ButtonGroup.setText(GroupGalleryItemList.get(position).getTitle());
                SelectedGroupID = GroupGalleryItemList.get(position).getItemID();
                Log.i("Info", "SelectedGroupID" + SelectedGroupID);
                RepresentativeListDataSetting();
            }
        });
    }
    private void RepresentativeListDataSetting() {
        RepresentativeList=CardService.getInstance().GetRepresentativeByGroupID(SelectedGroupID);
        RepresentativeGalleryItemList = new ArrayList<GalleryItem>();
        for(RepresentativeInfo Item:RepresentativeList)
        {
            RepresentativeGalleryItemList.add(new GalleryItem(Item.getRepresentativeID(),Item.getRepresentativeName(),Item.getRepresentativeImage()));
        }
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), RepresentativeGalleryItemList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ButtonRepresentative.setText(RepresentativeGalleryItemList.get(position).getTitle());
                SelectedRepresentativeID = RepresentativeGalleryItemList.get(position).getItemID();
                Log.i("Info", "SelectedRepresentativeID" + SelectedRepresentativeID);
                CardListDataSetting(SelectedRepresentativeID);
            }
        });
    }

    private void FavoriteCardListDataSetting() {
        CardList=CardService.getInstance().GetFavoriteCardsInfo();
        CardGalleryItemList = new ArrayList<GalleryItem>();
        for(CardInfo Item:CardList)
        {
            CardGalleryItemList.add(new GalleryItem(Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
        }
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), CardGalleryItemList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SelectedEntityCardID =CardGalleryItemList.get(position).getItemID();
                CardService.getInstance().ShowCardDetailDialog(SelectedEntityCardID, context);
                Log.i("Info", "SelectedEntityCardID" + SelectedEntityCardID);
            }
        });
    }

    private void CardListDataSetting(String RepresentativeID) {
        CardList=CardService.getInstance().GetCardsByRepresentativeID(RepresentativeID);
        CardGalleryItemList = new ArrayList<GalleryItem>();
        for(CardInfo Item:CardList)
        {
            CardGalleryItemList.add(new GalleryItem(Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
        }
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), CardGalleryItemList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SelectedEntityCardID =CardGalleryItemList.get(position).getItemID();
                CardService.getInstance().ShowCardDetailDialog(SelectedEntityCardID, context);
                Log.i("Info", "SelectedEntityCardID" + SelectedEntityCardID);
            }
        });
    }



    @OnClick(R.id.ButtonCategory)
    protected void onButtonClicked_ButtonCategory() {
        PopupMenu popupMenu = new PopupMenu(this, ButtonCategory);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    //Select all && Reset Menu
                    ResetMenuStatusForChangeCategory();
                    GategoryDataSetting();
                }
                else if (item.getItemId() == 2) {
                    //select Favorite
                    FavoriteCardListDataSetting();
                }
                else {
                    for (CategoryInfo CategoryInfoItem : CategoryList) {
                        if (CategoryInfoItem.getCategoryName().equals(item.getTitle().toString())) {
                            ButtonCategory.setText(item.getTitle().toString());
                            SelectedCategoryID = CategoryInfoItem.getCategoryID();
                            GroupDataSetting();
                        }
                    }
                }
                return false;
            }
        });
        popupMenu.getMenu().add(0,1,0,"All");
        popupMenu.getMenu().add(0, 2, 0, "Favorite");
        for(int i=0; i<CategoryList.size(); i++)
        {
            popupMenu.getMenu().add(CategoryList.get(i).getCategoryName());
        }
        popupMenu.show();
    }

    @OnClick(R.id.ButtonGroup)
    protected void onButtonClicked_ButtonGroup() {
        if(GroupGalleryItemList !=null)
        {

            PopupMenu popupMenu = new PopupMenu(this, ButtonGroup);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    ResetMenuStatusForChangeGroup();
                    for (GalleryItem GroupGalleryItem : GroupGalleryItemList) {
                        if (GroupGalleryItem.getTitle().equals(item.getTitle().toString())) {
                            ButtonGroup.setText(item.getTitle().toString());
                            SelectedGroupID = GroupGalleryItem.getItemID();
                            RepresentativeListDataSetting();
                        }
                    }
                    return false;
                }
            });
            for(int i=0; i<GroupGalleryItemList.size(); i++)
            {
                popupMenu.getMenu().add(GroupGalleryItemList.get(i).getTitle());
            }
            popupMenu.show();
        }
    }
    @OnClick(R.id.ButtonRepresentative)
    protected void onButtonClicked_ButtonRepresentative() {
        if(RepresentativeGalleryItemList !=null)
        {
            PopupMenu popupMenu = new PopupMenu(this, ButtonRepresentative);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    for (GalleryItem RepresentativeGalleryItem : RepresentativeGalleryItemList) {
                        if (RepresentativeGalleryItem.getTitle().equals(item.getTitle().toString())) {
                            ButtonRepresentative.setText(item.getTitle().toString());
                            SelectedRepresentativeID = RepresentativeGalleryItem.getItemID();
                            CardListDataSetting(SelectedRepresentativeID);
                        }
                    }
                    return false;
                }
            });
            for(int i=0; i<RepresentativeGalleryItemList.size(); i++)
            {
                popupMenu.getMenu().add(RepresentativeGalleryItemList.get(i).getTitle());
            }
            popupMenu.show();
        }
    }

    private void ResetMenuStatusForChangeCategory() {
        GroupGalleryItemList=null;
        RepresentativeGalleryItemList=null;
        ButtonCategory.setText("Category>>");
        ButtonGroup.setText("Group>>");
        ButtonRepresentative.setText("Member>>");
    }
    private void ResetMenuStatusForChangeGroup() {
        RepresentativeGalleryItemList=null;
        ButtonRepresentative.setText("Member>>");
    }
}
