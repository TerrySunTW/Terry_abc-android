package com.abc.terry_sun.abc;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupMenu;

import com.abc.terry_sun.abc.CustomClass.Adapter.AdapterCardsImage;
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
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
public class CardsActivity extends BasicActivity {

    @InjectView(R.id.gridView1)
    GridView gridView;
    @InjectView(R.id.ButtonCategory)
    Button ButtonCategory;
    @InjectView(R.id.ButtonGroup)
    Button ButtonGroup;
    @InjectView(R.id.ButtonRepresentative)
    Button ButtonRepresentative;

    @InjectView(R.id.view_position1)
    View view_position1;
    @InjectView(R.id.view_position2)
    View view_position2;
    @InjectView(R.id.view_position3)
    View view_position3;

    @InjectView(R.id.ViewIndication1)
    View ViewIndication1;
    @InjectView(R.id.ViewIndication2)
    View ViewIndication2;
    @InjectView(R.id.ViewIndication3)
    View ViewIndication3;



    List<CategoryInfo> CategoryList;
    List<GroupInfo> GroupList;
    List<RepresentativeInfo> RepresentativeList;
    List<CardInfo> CardList;

    List<GalleryItem> GalleryItemList;

    //MenuItem
    List<GalleryItem> GroupGalleryItemList=new ArrayList<GalleryItem>();
    List<GalleryItem> RepresentativeGalleryItemList=new ArrayList<GalleryItem>();;
    List<GalleryItem> CardGalleryItemList=new ArrayList<GalleryItem>();

    String SelectedCategoryID="";
    String SelectedGroupID="";
    String SelectedRepresentativeID="";
    String SelectedEntityCardID;

    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this.getParent();
        setContentView(R.layout.activity_cards);
        ButterKnife.inject(this);

        Log.i("INFO", "size.x=" + String.valueOf(ScreenService.GetScreenWidth(this)));
        int ColumnWidth=(ScreenService.GetScreenWidth(this).x - 30) / 3;
        gridView.setColumnWidth(ColumnWidth);
        gridView.setNumColumns(3);

    }
    @Override
    protected void onResume() {
        super.onResume();
        InitialParameter();
        //GategoryDataSetting();
        ShowCard();
    }
    void InitialParameter()
    {
        CategoryList = CardService.getInstance().GetAllCategory();
    }
    void ShowCard()
    {
        ShowCard(null);
    }
    void ShowCard(List<CardInfo> CardInfoList) {
        if(CardInfoList==null) {
            CardInfoList = CardService.getInstance().GetCardsByConditions(SelectedCategoryID, SelectedGroupID, SelectedRepresentativeID);
        }
        CardGalleryItemList.clear();
        for(CardInfo Item:CardInfoList)
        {
            CardGalleryItemList.add(new GalleryItem(
                    Item.getEntityCardID(),
                    Item.getCardName(),
                    Item.getCardImage(),
                    Item.getHasRealCard()
                    ));
        }
        gridView.setAdapter(new AdapterCardsImage(this.getParent(), CardGalleryItemList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SelectedEntityCardID = CardGalleryItemList.get(position).getItemID();
                CardService.getInstance().ShowCardDetailDialog(SelectedEntityCardID,null, context);
                Log.i("Info", "SelectedEntityCardID" + SelectedEntityCardID);
            }
        });
    }



    private void FavoriteCardListDataSetting() {
        CardList=CardService.getInstance().GetFavoriteCardsInfo();
        ShowCard(CardList);
    }

    void GetGroupGalleryItemList()
    {
        List<GroupInfo> GroupInfo=CardService.getInstance().GetGroupByCategoryID(SelectedCategoryID);
        GroupGalleryItemList.clear();
        for(GroupInfo Item:GroupInfo)
        {
            GroupGalleryItemList.add(new GalleryItem(
                    Item.getGroupID(),
                    Item.getGroupName(),
                    Item.getGroupImage()
            ));
        }
    }
    void GetRepresentativeList()
    {
        List<RepresentativeInfo> RepresentativeInfo=CardService.getInstance().GetRepresentativeByGroupID(SelectedGroupID);
        RepresentativeGalleryItemList.clear();
        for(RepresentativeInfo Item:RepresentativeInfo)
        {
            RepresentativeGalleryItemList.add(new GalleryItem(
                    Item.getRepresentativeID(),
                    Item.getRepresentativeName(),
                    Item.getRepresentativeImage()
            ));
        }
    }
    @OnClick(R.id.ButtonCategory)
    protected void onButtonClicked_ButtonCategory() {

        PopupMenu popupMenu = new PopupMenu(this, view_position1);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    //Select all && Reset Menu
                    ResetMenuStatusForChangeCategory();
                    ShowCard();
                } else if (item.getItemId() == 2) {
                    //select Favorite
                    ButtonCategory.setText("Favorite");
                    FavoriteCardListDataSetting();
                } else {
                    for (CategoryInfo CategoryInfoItem : CategoryList) {
                        if (CategoryInfoItem.getCategoryName().equals(item.getTitle().toString())) {
                            ButtonCategory.setText(item.getTitle().toString());
                            SelectedCategoryID = CategoryInfoItem.getCategoryID();
                            GetGroupGalleryItemList();
                            ShowCard();
                        }
                    }
                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
                //ButtonCategory.setShowOutline(false);
                ViewIndication1.setVisibility(View.INVISIBLE);
            }
        });
        popupMenu.getMenu().add(0,1,0,"All");
        popupMenu.getMenu().add(0, 2, 0, "Favorite");
        for(int i=0; i<CategoryList.size(); i++)
        {
            popupMenu.getMenu().add(CategoryList.get(i).getCategoryName());
        }
        popupMenu.show();
        //ButtonCategory.setShowOutline(true);
        ViewIndication1.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ButtonGroup)
    protected void onButtonClicked_ButtonGroup() {

        if(GroupGalleryItemList !=null && GroupGalleryItemList.size()>0)
        {

            PopupMenu popupMenu = new PopupMenu(this, view_position2);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    ResetMenuStatusForChangeGroup();
                    for (GalleryItem GroupGalleryItem : GroupGalleryItemList) {
                        if (GroupGalleryItem.getTitle().equals(item.getTitle().toString())) {
                            ButtonGroup.setText(item.getTitle().toString());
                            SelectedGroupID = GroupGalleryItem.getItemID();
                            GetRepresentativeList();
                            ShowCard();
                        }
                    }
                    return false;
                }
            });
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu popupMenu) {
                    ViewIndication2.setVisibility(View.INVISIBLE);
                }
            });
            for(int i=0; i<GroupGalleryItemList.size(); i++)
            {
                popupMenu.getMenu().add(GroupGalleryItemList.get(i).getTitle());
            }
            popupMenu.show();
            //ButtonGroup.setShowOutline(true);
            ViewIndication2.setVisibility(View.VISIBLE);
        }
    }
    @OnClick(R.id.ButtonRepresentative)
    protected void onButtonClicked_ButtonRepresentative() {

        if(RepresentativeGalleryItemList !=null && RepresentativeGalleryItemList.size()>0)
        {
            PopupMenu popupMenu = new PopupMenu(this, view_position3);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    for (GalleryItem RepresentativeGalleryItem : RepresentativeGalleryItemList) {
                        if (RepresentativeGalleryItem.getTitle().equals(item.getTitle().toString())) {
                            ButtonRepresentative.setText(item.getTitle().toString());
                            SelectedRepresentativeID = RepresentativeGalleryItem.getItemID();
                            ShowCard();
                        }
                    }
                    return false;
                }
            });
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu popupMenu) {
                    //ButtonRepresentative.setShowOutline(false);
                    ViewIndication3.setVisibility(View.INVISIBLE);
                }
            });
            for(int i=0; i<RepresentativeGalleryItemList.size(); i++)
            {
                popupMenu.getMenu().add(RepresentativeGalleryItemList.get(i).getTitle());
            }
            popupMenu.show();
            //ButtonRepresentative.setShowOutline(true);
            ViewIndication3.setVisibility(View.VISIBLE);
        }
    }

    private void ResetMenuStatusForChangeCategory() {
        SelectedCategoryID="";
        SelectedGroupID="";
        SelectedRepresentativeID="";
        GroupGalleryItemList.clear();
        RepresentativeGalleryItemList.clear();
        ButtonCategory.setText("All");
        ButtonGroup.setText("Group>>");
        ButtonRepresentative.setText("Member>>");
    }
    private void ResetMenuStatusForChangeGroup() {
        SelectedRepresentativeID="";
        RepresentativeGalleryItemList.clear();
        ButtonRepresentative.setText("Member>>");
    }
}
