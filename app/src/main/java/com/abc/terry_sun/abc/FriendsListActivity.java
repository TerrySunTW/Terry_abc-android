package com.abc.terry_sun.abc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.abc.terry_sun.abc.CustomClass.Adapter.Adapter_FriendsList;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.FriendService;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.abc.terry_sun.abc.Service.StorageService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FriendsListActivity extends Fragment {
	/** Called when the activity is first created. */

	@InjectView(R.id.listview_activity_list)
	ListView listview_activity_list;
	@InjectView(R.id.TextView_Name)
	TextView TextViewName;
	@InjectView(R.id.TextView_Content)
	TextView TextViewCard;

	@InjectView(R.id.ButtonAddFriendID)
	Button ButtonAddFriend;
	@InjectView(R.id.MyButton)
	ImageButton MyImageButton;

	final String TAG="FriendsListActivity";
	static Adapter_FriendsList _Adapter_FriendsList;

	static Context context;
	private View mRootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null){
			mRootView = inflater.inflate(R.layout.activity_friends_list,container,false);
		}
		context=getActivity();
		ButterKnife.inject(this, mRootView);
		InitialParameter();
		List_Setting();
		return mRootView;
	}


	private void List_Setting() {
		Log.e(TAG, "List_Setting()");
		_Adapter_FriendsList=new Adapter_FriendsList(getActivity());
		listview_activity_list.setAdapter(_Adapter_FriendsList);
		listview_activity_list.deferNotifyDataSetChanged();

	}
	void InitialParameter()
	{
		DB_Friend _DB_Friend= FriendService.getInstance().GetMyInfo();
		TextViewName.setText(_DB_Friend.getFriendName() + "(ID:"+_DB_Friend.getFriendID()+")");
		TextViewCard.setText("Card:"+_DB_Friend.getCardCount());
		ButtonAddFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FriendService.getInstance().AddFriendDialog();
			}
		});
		MyImageButton.setImageBitmap(
				ImageService.GetBitmapFromPath(StorageService.GetFriendImagePath(_DB_Friend.getFriendID()))
		);
	}
	public static void UpdateList()
	{
		_Adapter_FriendsList.UpdateData();
		_Adapter_FriendsList.notifyDataSetChanged();
	}
	@OnClick(R.id.ButtonSetting)
	public void GoSettingActivity() {
		Log.e(TAG, "GoSettingActivity");
		((BaseFragment) getParentFragment()).replaceFragment(new SettingActivity(), true);
	}


}
