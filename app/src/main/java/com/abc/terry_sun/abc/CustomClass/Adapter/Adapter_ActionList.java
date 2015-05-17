package com.abc.terry_sun.abc.CustomClass.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abc.terry_sun.abc.Models.ListItem_Actions;
import com.abc.terry_sun.abc.R;

import java.util.List;

/**
 * Created by Terry on 2015/5/14.
 */
public class Adapter_ActionList extends BaseAdapter {
    private Activity activity;
    private List<ListItem_Actions> stringPairList;


    public Adapter_ActionList(Activity activity, List<ListItem_Actions> stringPairList) {
        super();
        this.activity = activity;
        this.stringPairList = stringPairList;
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
            convertView = inflater.inflate(R.layout.activity_actions_item, null);
        }
        TextView ItemInfo1 = (TextView) convertView.findViewById(R.id.ItemInfo1);
        TextView ItemInfo2 = (TextView) convertView.findViewById(R.id.ItemInfo2);

        ItemInfo1.setText(((ListItem_Actions)stringPairList.get(position)).getTitle1());
        ItemInfo2.setText(((ListItem_Actions)stringPairList.get(position)).getTitle2());

        return convertView;
    }
}
