package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.abc.terry_sun.abc.CustomClass.Adapter.Adapter_FriendsList;
import com.abc.terry_sun.abc.Models.ListItem_Actions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FriendsListActivity extends Activity {
	/** Called when the activity is first created. */

	@InjectView(R.id.listview_activity_list)
	ListView listview_activity_list;


	Adapter_FriendsList _Adapter_FriendsList;
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
		List<ListItem_Actions> data=new ArrayList<ListItem_Actions>();
		_Adapter_FriendsList=new Adapter_FriendsList(this);
		listview_activity_list.setAdapter(_Adapter_FriendsList);
		listview_activity_list.deferNotifyDataSetChanged();
	}
	void InitialParameter()
	{

	}
}
