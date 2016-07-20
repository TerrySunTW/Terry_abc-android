package com.abc.terry_sun.abc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.abc.terry_sun.abc.CustomClass.Adapter.Adapter_BonusList;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BonusListActivity extends BaseFragment {
	/** Called when the activity is first created. */
	@BindView(R.id.listview_activity_list)ListView listview_activity_list;

	@BindView(R.id.swipe)SwipeRefreshLayout swipeRefreshLayout;

	@BindView(R.id.ButtonCategory)Button ButtonCategory;
	@BindView(R.id.ButtonGroup)Button ButtonGroup;
	@BindView(R.id.ButtonRepresentative)Button ButtonRepresentative;

	@BindView(R.id.view_position1)View view_position1;
	@BindView(R.id.view_position2)	View view_position2;
	@BindView(R.id.view_position3)	View view_position3;

	@BindView(R.id.ViewIndication1)View ViewIndication1;
	@BindView(R.id.ViewIndication2)View ViewIndication2;
	@BindView(R.id.ViewIndication3)View ViewIndication3;

	List<CategoryInfo> CategoryList;
	List<GroupInfo> GroupList;
	List<RepresentativeInfo> RepresentativeList;
	List<CardInfo> CardList;


	//MenuItem
	List<GalleryItem> CategoryGalleryItemList;
	List<GalleryItem> GroupGalleryItemList;
	List<GalleryItem> RepresentativeGalleryItemList;
	List<GalleryItem> CardGalleryItemList;

	static String SelectedCategoryID="";
	static String SelectedGroupID="";
	static String SelectedRepresentativeID="";
	Adapter_BonusList _Adapter_ActionList;
	static boolean IsFavorate=false;
	static Context context;
	private View mRootView;
	final static String TAG="BonusListActivity";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null){
			mRootView = inflater.inflate(R.layout.activity_bonus_list,container,false);
		}
		context=getActivity();
		ButterKnife.bind(this, mRootView);
		List_Setting();
		InitialParameter();
		return mRootView;
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
				swipeRefreshLayout.setRefreshing(false);
				List_Setting();
			}
		}).execute();
	}
	private void List_Setting() {
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.i(TAG, "onRefresh");
				swipeRefreshLayout.setRefreshing(true);
				UpdateServerData();
			}
		});
		_Adapter_ActionList=new Adapter_BonusList(getActivity());
		listview_activity_list.setAdapter(_Adapter_ActionList);
		Update_List();
	}
	public void Update_List() {
		Log.i(TAG, "Update_List");
		Log.i(TAG, "IsFavorate=" + String.valueOf(IsFavorate));
		if(IsFavorate)
		{
			_Adapter_ActionList.UpdateData(CardService.getInstance().GetFavorateCards());
		}
		else
		{
			Log.i(TAG, "SelectedCategoryID="+ String.valueOf(SelectedCategoryID));
			Log.i(TAG, "SelectedGroupID="+ String.valueOf(SelectedGroupID));
			Log.i(TAG, "SelectedRepresentativeID="+ String.valueOf(SelectedRepresentativeID));
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
		CardList=CardService.getInstance().GetCardsByRepresentativeID(SelectedGroupID,RepresentativeID);
		CardGalleryItemList = new ArrayList<GalleryItem>();
		for(CardInfo Item:CardList)
		{
			CardGalleryItemList.add(new GalleryItem(Item.getEntityCardID(),Item.getCardName(),Item.getCardImage()));
		}
	}

	@OnClick(R.id.ButtonCategory)
	protected void onButtonClicked_ButtonCategory() {
		PopupMenu popupMenu = new PopupMenu(context, view_position1);
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Log.i(TAG, "onMenuItemClick item.getItemId()="+String.valueOf(item.getItemId()));
				ResetMenuStatusForChangeCategory();
				if (item.getItemId() == 1) {
					SelectedCategoryID="";
					ButtonCategory.setText("All");
					//Select all && Reset Menu
				} else if (item.getItemId() == 2) {
					//select Favorite
					ButtonCategory.setText("Favorite");
					IsFavorate = true;
				} else {
					Log.i(TAG, "CategoryList CategoryList.size()=" + String.valueOf(CategoryList.size()));
					for (CategoryInfo CategoryInfoItem : CategoryList) {
						if (CategoryInfoItem.getCategoryName().equals(item.getTitle().toString())) {
							ButtonCategory.setText(item.getTitle().toString());
							SelectedCategoryID = CategoryInfoItem.getCategoryID();
							GroupDataSetting();
							break;
						}
					}
				}
				Update_List();

				return false;
			}
		});
		popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
			@Override
			public void onDismiss(PopupMenu popupMenu) {
				//ButtonCategory.setShowOutline(false);
				//ViewIndication1.setVisibility(View.INVISIBLE);
			}
		});
		popupMenu.getMenu().add(0,1,0,"All");
		popupMenu.getMenu().add(0, 2, 0, "Favorite");
		for(int i=0; i<CategoryList.size(); i++)
		{
			popupMenu.getMenu().add(CategoryList.get(i).getCategoryName());
		}
		popupMenu.show();
		ViewIndication1.setVisibility(View.VISIBLE);
		ViewIndication2.setVisibility(View.INVISIBLE);
		ViewIndication3.setVisibility(View.INVISIBLE);
	}

	@OnClick(R.id.ButtonGroup)
	protected void onButtonClicked_ButtonGroup() {
		if(GroupGalleryItemList !=null)
		{

			PopupMenu popupMenu = new PopupMenu(context, view_position2);
			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					ResetMenuStatusForChangeGroup();
					for (GalleryItem GroupGalleryItem : GroupGalleryItemList) {
						if (GroupGalleryItem.getTitle().equals(item.getTitle().toString())) {
							ButtonGroup.setText(item.getTitle().toString());
							SelectedGroupID = GroupGalleryItem.getItemID();
							IsFavorate = false;
							Update_List();
							RepresentativeListDataSetting();
						}
					}
					return false;
				}
			});
			popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
				@Override
				public void onDismiss(PopupMenu popupMenu) {
					//ViewIndication2.setVisibility(View.INVISIBLE);
				}
			});
			for(int i=0; i<GroupGalleryItemList.size(); i++)
			{
				popupMenu.getMenu().add(GroupGalleryItemList.get(i).getTitle());
			}
			popupMenu.show();
			ViewIndication2.setVisibility(View.VISIBLE);
			ViewIndication3.setVisibility(View.INVISIBLE);
		}
	}
	@OnClick(R.id.ButtonRepresentative)
	protected void onButtonClicked_ButtonRepresentative() {
		if(RepresentativeGalleryItemList !=null)
		{
			PopupMenu popupMenu = new PopupMenu(context, view_position3);
			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					for (GalleryItem RepresentativeGalleryItem : RepresentativeGalleryItemList) {
						if (RepresentativeGalleryItem.getTitle().equals(item.getTitle().toString())) {
							ButtonRepresentative.setText(item.getTitle().toString());
							SelectedRepresentativeID = RepresentativeGalleryItem.getItemID();
							IsFavorate = false;
							Update_List();
							CardListDataSetting(SelectedRepresentativeID);
						}
					}
					return false;
				}
			});
			popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
				@Override
				public void onDismiss(PopupMenu popupMenu) {
					//ViewIndication3.setVisibility(View.INVISIBLE);
				}
			});
			for(int i=0; i<RepresentativeGalleryItemList.size(); i++)
			{
				popupMenu.getMenu().add(RepresentativeGalleryItemList.get(i).getTitle());
			}
			popupMenu.show();
			ViewIndication3.setVisibility(View.VISIBLE);
		}
	}

	private void ResetMenuStatusForChangeCategory() {
		IsFavorate = false;
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
