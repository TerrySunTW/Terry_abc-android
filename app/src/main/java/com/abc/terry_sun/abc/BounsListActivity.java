package com.abc.terry_sun.abc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.abc.terry_sun.abc.CustomClass.Adapter.Adapter_ActionList;
import com.abc.terry_sun.abc.Models.ListItem_Actions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BounsListActivity extends Activity {
	/** Called when the activity is first created. */
	Handler messageHandler;
	@InjectView(R.id.listview_activity_list)
	ListView listview_activity_list;
	String[][] TableArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("Event", "onCreate");
		setContentView(R.layout.activity_bouns_list);
		super.onCreate(savedInstanceState);
		ButterKnife.inject(this);
		HandlerSetting();
		UI_Setting();
	}

	private void UI_Setting() {
		List<ListItem_Actions> data=new ArrayList<ListItem_Actions>();
		data.add(new ListItem_Actions("10% off Baseball Game ticket(Y/X)",""));
		data.add(new ListItem_Actions("簽名球一顆(95/100)",""));
		listview_activity_list.setAdapter(new Adapter_ActionList(this, data));
	}

	@Override
	public void onResume(){
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	private void HandlerSetting() {
		messageHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 0:
						break;
					default:
						//login fail
						break;
				}
				super.handleMessage(msg);
			}
		};
	}
}
