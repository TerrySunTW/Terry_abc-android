package com.abc.terry_sun.abc.CustomClass.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.Models.ListItem_Friend;
import com.abc.terry_sun.abc.R;
import com.abc.terry_sun.abc.Service.FriendService;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.StorageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terry on 2015/5/14.
 */
public class Adapter_FriendsList extends BaseAdapter {
    private Activity activity;
    private List<DB_Friend> stringPairList=new ArrayList<DB_Friend>();


    public Adapter_FriendsList(Activity activity) {
        super();
        this.activity = activity;
        stringPairList=FriendService.getInstance().GetAllFriends();
    }

    @Override
    public int getCount() {
        return stringPairList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringPairList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_friends_item, null);
        }

        DB_Friend _ListItem_Friend=stringPairList.get(position);
        ImageButton FriendButton = (ImageButton) convertView.findViewById(R.id.FriendButton);
        FriendButton.setImageBitmap(
                ImageService.GetBitmapFromPath(StorageService.GetFriendImagePath(_ListItem_Friend.getFriendID()))
                );


        TextView TextView_Name = (TextView) convertView.findViewById(R.id.TextView_Name);
        TextView TextView_Content = (TextView) convertView.findViewById(R.id.TextView_Content);

        TextView_Name.setText(_ListItem_Friend.getFriendName());
        TextView_Content.setText("Card:"+_ListItem_Friend.getCardCount());

        if(position==0)
        {
            TextView_Name.setBackgroundColor(Color.BLUE);
        }

        return convertView;
    }
}
