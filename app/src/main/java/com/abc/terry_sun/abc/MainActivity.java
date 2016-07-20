package com.abc.terry_sun.abc;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.ProcessControlService;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
	final static String TAG="MainActivity";
	public static final String TAB1  = "tab_1_identifier";
	public static final String TAB2  = "tab_2_identifier";
	public static final String TAB3  = "tab_3_identifier";
	public static final String TAB4  = "tab_4_identifier";
	public static final String TAB5  = "tab_5_identifier";

	static ImageButton MainCardImageButton;
	static Context MainActivityContext;

	private FragmentTabHost mTabHost;
	private com.abc.terry_sun.abc.CustomClass.ViewPager.MyViewPager mViewPager;
	private int OldPagePosition=0;
	private List<Fragment> mFragmentList;
	private Class mClass[] = {CardsActivity.class,BonusListActivity.class,Fragment.class,FriendsContainerFragment.class,R_CardContainerFragment.class};
	private Fragment mFragment[] = {new CardsActivity(),new BonusListActivity(),new Fragment(),new FriendsContainerFragment(),new R_CardContainerFragment()};
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
		MainActivityContext=this;
		MainCardImageButton=(ImageButton) findViewById(R.id.ImageButtonMainCard);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		DB_Cards MainCard= CardService.getInstance().GetMainCards();
		if(MainCard!=null) {
			Log.i(TAG, "MainCardID=" + String.valueOf(MainCard.getCardID()));
			ChangeMainCardImage(ImageService.GetBitmapFromImageName(MainCard.getCardImage()), MainCard.getEntityCardID());
		}

		initView();
		initEvent();
	}

	private void initView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mViewPager = (com.abc.terry_sun.abc.CustomClass.ViewPager.MyViewPager) findViewById(R.id.view_pager);
		mViewPager.setPagingEnabled(false);
		mViewPager.setOffscreenPageLimit(0);
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
				int TempPosition=position;
				return mFragmentList.get(TempPosition);
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
		mTabHost.getTabWidget().getChildAt(3).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((BaseFragment)mFragment[3]).GoFirstFragment();
				mTabHost.setCurrentTab(3);
			}
		});
		mTabHost.getTabWidget().getChildAt(4).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((BaseFragment) mFragment[4]).GoFirstFragment();
				mTabHost.setCurrentTab(4);
			}
		});

		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				int CurrentTab = mTabHost.getCurrentTab();
				Log.i(TAG, "onTabChanged,CurrentTab=" + String.valueOf(CurrentTab));
				mViewPager.setCurrentItem(CurrentTab);
				if (CurrentTab == 3) {
					((BaseFragment) mFragment[3]).GoFirstFragment();
				} else if (CurrentTab == 4) {
					((BaseFragment) mFragment[4]).GoFirstFragment();
				}

			}
		});
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				int tempPosition=OldPagePosition;
				int newPosition=position;
				if(position==2)
				{
					if(tempPosition==1)
					{
						newPosition=3;
					}
					else
					{
						newPosition=1;
					}
				}
				OldPagePosition=newPosition;
				mTabHost.setCurrentTab(newPosition);
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
		Log.i(TAG, "EntityCardID=" + String.valueOf(EntityCardID));
		Log.i(TAG, "MainCardImage.getByteCount=" + String.valueOf(MainCardImage.getByteCount()));
		MainCardImageButton.setImageBitmap(MainCardImage);
		MainCardImageButton.setTag(EntityCardID);
		MainCardImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CardService.getInstance().ShowCardDetailDialog(EntityCardID, null, MainActivityContext, true);
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		Log.i(TAG, "onBackPressed");
		if(mTabHost!=null)
		{
			if(!((BaseFragment)mFragment[mTabHost.getCurrentTab()]).GobackFragment())
			{
				ProcessControlService.QuitProcess(this);
			}
		}
		else
		{
			ProcessControlService.QuitProcess(this);
		}

	}
}
