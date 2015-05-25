package com.abc.terry_sun.abc;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;
import android.widget.GridView;
import android.widget.Toast;

public class Photo_GalleryActivity extends Activity {
	public  GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_main);
		
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new Photo_ImageAdapter(this));
		
	}
	private Boolean exit = false;
	@Override
	public void onBackPressed() {
		if (exit) {
			finish(); // finish activity
		} else {
			Toast.makeText(this, "Press Back again to Exit.",
					Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);

		}
	}
}
