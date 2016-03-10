package com.abc.terry_sun.abc;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.abc.terry_sun.abc.CustomClass.Application.ABCApplication;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.ProcessControlService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {
	final static String TAG="MainActivity";
	/** Called when the activity is first created. */
	@InjectView(R.id.ImageButtonMainCard)
	ImageButton ImageButtonMainCard;




	public static final String TAB1  = "tab_1_identifier";
	public static final String TAB2  = "tab_2_identifier";
	public static final String TAB3  = "tab_3_identifier";
	public static final String TAB4  = "tab_4_identifier";
	public static final String TAB5  = "tab_5_identifier";

	static ImageButton MainCardImageButton;
	static Context MainActivityContext;

	private FragmentTabHost mTabHost;
	private ViewPager mViewPager;
	private List<Fragment> mFragmentList;
	private Class mClass[] = {CardsActivity.class,BonusListActivity.class,R_CardHomeActivity.class,FriendsContainerFragment.class,R_CardContainerFragment.class};
	private Fragment mFragment[] = {new CardsActivity(),new BonusListActivity(),new R_CardHomeActivity(),new FriendsContainerFragment(),new R_CardContainerFragment()};
	private String mTitles[] = {"Cards","Bonus","MainCard","Link","R-Card"};
	private int mImages[] = {
			R.drawable.tab_cards,
			R.drawable.tab_bonus,
			R.drawable.tab_cards,
			R.drawable.tab_link,
			R.drawable.tab_r_card
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ButterKnife.inject(this);
		MainActivityContext=this;
		MainCardImageButton=ImageButtonMainCard;


		DB_Cards MainCard= CardService.getInstance().GetMainCards();
		if(MainCard!=null) {
			ChangeMainCardImage(ImageService.GetBitmapFromImageName(MainCard.getCardImage()), MainCard.getEntityCardID());
		}

		initView();
		initEvent();
	}

	private void initView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);

		mFragmentList = new ArrayList<Fragment>();

		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);

		for (int i = 0;i < mFragment.length;i++){
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTitles[i]).setIndicator(getTabView(i));
			mTabHost.addTab(tabSpec, mClass[i], null);
			mFragmentList.add(mFragment[i]);
		}

		mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return mFragmentList.get(position);
			}

			@Override
			public int getCount() {
				return mFragmentList.size();
			}
		});
	}

	private View getTabView(int index) {
		View view = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(mTitles[index]);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		icon.setImageResource(mImages[index]);
		return view;
	}

	private void initEvent() {

		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				mViewPager.setCurrentItem(mTabHost.getCurrentTab());
			}
		});

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				mTabHost.setCurrentTab(position);

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}

	public static Context GetMainActivityContext()
	{
		return MainActivityContext;
	}
	public static void ChangeMainCardImage(final Bitmap MainCardImage,final String EntityCardID)
	{
		//default NFC Card ID
		MainCardImageButton.setImageBitmap(MainCardImage);
		MainCardImageButton.setTag(EntityCardID);
		MainCardImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CardService.getInstance().ShowCardDetailDialog(EntityCardID, "Main Card Message...", MainActivityContext, true);
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		Log.i(TAG, "onBackPressed");
		if(mTabHost!=null)
		{
			((BaseFragment)mFragment[mTabHost.getCurrentTab()]).GobackFragment();
		}

	}
}
