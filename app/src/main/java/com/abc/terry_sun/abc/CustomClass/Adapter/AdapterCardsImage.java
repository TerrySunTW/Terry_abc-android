package com.abc.terry_sun.abc.CustomClass.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.terry_sun.abc.Models.GalleryItem;
import com.abc.terry_sun.abc.R;
import com.abc.terry_sun.abc.Service.StorageService;

import java.util.List;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class AdapterCardsImage extends BaseAdapter {
    Context context;
    List<GalleryItem> GalleryItemList;

    public AdapterCardsImage(Context context, List<GalleryItem> _GalleryItemList) {
        this.context = context;
        this.GalleryItemList = _GalleryItemList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.activity_cards_item, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(GalleryItemList.get(position).getTitle());

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            Log.i("Info", "ImagePath:" + StorageService.GetImagePath(GalleryItemList.get(position).getImageName()));
            Bitmap Img = BitmapFactory.decodeFile(StorageService.GetImagePath(GalleryItemList.get(position).getImageName()));
            imageView.setImageBitmap(Img);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return GalleryItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
