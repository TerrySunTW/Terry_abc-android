package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.abc.terry_sun.abc.CustomClass.Adapter.Adapter_BonusList;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.Models.CardInfo;
import com.abc.terry_sun.abc.Models.CategoryInfo;
import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.Models.GroupInfo;
import com.abc.terry_sun.abc.Models.ListItem_Actions;
import com.abc.terry_sun.abc.Models.RepresentativeInfo;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class BonusListActivity extends Activity {
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

	static String SelectedCategoryID="";
	static String SelectedGroupID="";
	static String SelectedRepresentativeID="";
	String SelectedEntityCardID;
	static Adapter_BonusList _Adapter_ActionList;
	static boolean IsFavorate=false;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("Event", "onCreate");
		setContentView(R.layout.activity_bonus_list);
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
		UpdateServerData();
		InitialParameter();

	}
	private void UpdateServerData() {
		new AsyncTaskHttpRequest(MainActivity.MainActivityContext, new AsyncTaskProcessingInterface() {
			@Override
			public void DoProcessing() {
				ServerCommunicationService.getInstance().UpdateServerInfo();
			}
		}, new AsyncTaskPostProcessingInterface() {
			@Override
			public void DoProcessing() {
				List_Setting();
			}
		}).execute();
	}
	private void List_Setting() {
		List<ListItem_Actions> data=new ArrayList<ListItem_Actions>();
		List<DB_Cards> AllCards = CardService.getInstance().GetAllCards();

		_Adapter_ActionList=new Adapter_BonusList(this, AllCards);
		listview_activity_list.setAdapter(_Adapter_ActionList);
	}
	public static void Update_List() {
		if(IsFavorate)
		{
			//_Adapter_ActionList.UpdateData(CardService.getInstance().GetFavorateEvents());
			_Adapter_ActionList.UpdateData(CardService.getInstance().GetFavorateCards());
		}
		else
		{
			_Adapter_ActionList.UpdateData(CardService.getInstance().GetFilteredCards(SelectedCategoryID, SelectedGroupID, SelectedRepresentativeID));
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
					IsFavorate=false;
					Update_List();
				}
				else if (item.getItemId() == 2) {
					//select Favorite
					IsFavorate=true;
					Update_List();
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
							IsFavorate=false;
							Update_List();
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
							IsFavorate=false;
							Update_List();
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
