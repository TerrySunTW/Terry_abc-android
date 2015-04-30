package com.abc.terry_sun.abc;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.GridView;

public class Photo_GalleryActivity extends Activity {
	public  GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_main);
		
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new Photo_ImageAdapter(this));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
