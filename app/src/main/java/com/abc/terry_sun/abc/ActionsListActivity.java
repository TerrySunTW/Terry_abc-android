package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.abc.terry_sun.abc.CustomClass.Adapter.AdapterCardsImage;
import com.abc.terry_sun.abc.CustomClass.Adapter.Adapter_ActionList;
import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.Entities.Events;
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.ListItem_Actions;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ImageService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ActionsListActivity extends Activity {
	/** Called when the activity is first created. */
	Handler messageHandler;
	@InjectView(R.id.listview_activity_list)
	ListView listview_activity_list;
	String[][] TableArray;
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

	String SelectedCategoryID="";
	String SelectedGroupID="";
	String SelectedRepresentativeID="";
	String SelectedEntityCardID;
	Adapter_ActionList _Adapter_ActionList;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("Event", "onCreate");
		setContentView(R.layout.activity_actions_list);
		super.onCreate(savedInstanceState);
		ButterKnife.inject(this);

	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
		InitialParameter();
		GategoryDataSetting();
		List_Setting();
	}
	private void List_Setting() {
		List<ListItem_Actions> data=new ArrayList<ListItem_Actions>();
		List<Events> CardEvents = CardService.getInstance().GetAllEvents();

		_Adapter_ActionList=new Adapter_ActionList(this,CardEvents);
		listview_activity_list.setAdapter(_Adapter_ActionList);
	}
	private void Update_List(Boolean IsFavorate) {
		if(IsFavorate)
		{
			_Adapter_ActionList.UpdateData(CardService.getInstance().GetFavorateEvents());
		}
		else
		{
			_Adapter_ActionList.UpdateData(CardService.getInstance().GetFilteredEvents(SelectedCategoryID, SelectedGroupID, SelectedRepresentativeID));
		}
		_Adapter_ActionList.notifyDataSetChanged();
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

	}

	private void GroupDataSetting() {
		GroupList=CardService.getInstance().GetGroupByCategoryID(SelectedCategoryID);
		GroupGalleryItemList = new ArrayList<GalleryItem>();
		for(GroupInfo Item:GroupList)
		{
			GroupGalleryItemList.add(new GalleryItem(Item.getGroupID(),Item.getGroupName(),Item.getGroupImage()));
		}
	}
	private void RepresentativeListDataSetting() {
		RepresentativeList=CardService.getInstance().GetRepresentativeByGroupID(SelectedGroupID);
		RepresentativeGalleryItemList = new ArrayList<GalleryItem>();
		for(RepresentativeInfo Item:RepresentativeList)
		{
			RepresentativeGalleryItemList.add(new GalleryItem(Item.getRepresentativeID(),Item.getRepresentativeName(),Item.getRepresentativeImage()));
		}

	}

	private void CardListDataSetting(String RepresentativeID) {
		CardList=CardService.getInstance().GetCardsByRepresentativeID(RepresentativeID);
		CardGalleryItemList = new ArrayList<GalleryItem>();
		for(CardInfo Item:CardList)
		{
			CardGalleryItemList.add(new GalleryItem(Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
		}

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
					Update_List(false);
				}
				else if (item.getItemId() == 2) {
					//select Favorite
					Update_List(true);
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
							Update_List(false);
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
							Update_List(false);
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
		SelectedCategoryID="";
		SelectedGroupID="";
		SelectedRepresentativeID="";
		ButtonCategory.setText("Category>>");
		ButtonGroup.setText("Group>>");
		ButtonRepresentative.setText("Member>>");
	}
	private void ResetMenuStatusForChangeGroup() {
		RepresentativeGalleryItemList=null;
		ButtonRepresentative.setText("Member>>");
	}


}
