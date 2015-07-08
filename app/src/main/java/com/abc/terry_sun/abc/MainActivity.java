package com.abc.terry_sun.abc;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.StorageService;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@InjectView(R.id.ImageButtonMainCard)
	ImageButton ImageButtonMainCard;
	static ImageButton MainCardImageButton;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ButterKnife.inject(this);
		MainCardImageButton=ImageButtonMainCard;

		Cards MainCard= CardService.getInstance().GetMainCards();
		ChangeMainCardImage(ImageService.GetBitmapFromImageName(MainCard.getCardImage()));
		setTabs();
	}

	public static void ChangeMainCardImage(Bitmap MainCardImage)
	{
		MainCardImageButton.setImageBitmap(MainCardImage);
	}
	private void setTabs() {
		addTab("Cards", R.drawable.tab_home, TabGroup_Cards.class);
		addTab("Bonus", R.drawable.tab_search, ActionsListActivity.class);
		addTab("Fake", R.drawable.tab_search, CardDetailEmulateActivity.class);
        addTab("Link", R.drawable.tab_search, OptionsActivity.class);
		addTab("R-Card", R.drawable.tab_search, SettingActivity.class);
	}
	
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}
