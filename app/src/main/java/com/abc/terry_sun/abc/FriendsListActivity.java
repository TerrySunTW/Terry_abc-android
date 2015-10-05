package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.abc.terry_sun.abc.CustomClass.Adapter.Adapter_FriendsList;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.Models.ListItem_Actions;
import com.abc.terry_sun.abc.Service.FriendService;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.StorageService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FriendsListActivity extends Activity {
	/** Called when the activity is first created. */

	@InjectView(R.id.listview_activity_list)
	ListView listview_activity_list;
	@InjectView(R.id.TextViewName)
	TextView TextViewName;
	@InjectView(R.id.TextViewCard)
	TextView TextViewCard;
	@InjectView(R.id.TextViewID)
	TextView TextViewID;
	@InjectView(R.id.FriendButton)
	ImageButton FriendButton;

	@InjectView(R.id.ButtonAddFriend)
	Button ButtonAddFriend;

	static Adapter_FriendsList _Adapter_FriendsList;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("Event", "onCreate");
		setContentView(R.layout.activity_friends_list);
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
		List_Setting();
	}
	private void List_Setting() {
		_Adapter_FriendsList=new Adapter_FriendsList(this);
		listview_activity_list.setAdapter(_Adapter_FriendsList);
		listview_activity_list.deferNotifyDataSetChanged();
	}
	void InitialParameter()
	{
		DB_Friend _DB_Friend= FriendService.getInstance().GetMyInfo();
		TextViewName.setText(_DB_Friend.getFriendName());
		TextViewCard.setText(_DB_Friend.getCardCount());
		TextViewID.setText(_DB_Friend.getFriendID());
		FriendButton.setImageBitmap(
				ImageService.GetBitmapFromPath(StorageService.GetFriendImagePath(_DB_Friend.getFriendID()))
		);
		ButtonAddFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FriendService.getInstance().AddFriendDialog();
			}
		});
	}
	public static void UpdateList()
	{
		_Adapter_FriendsList.UpdateData();
		_Adapter_FriendsList.notifyDataSetChanged();
	}
}
