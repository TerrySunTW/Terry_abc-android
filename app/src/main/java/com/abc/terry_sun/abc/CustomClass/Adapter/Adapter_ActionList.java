package com.abc.terry_sun.abc.CustomClass.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.Events;
import com.abc.terry_sun.abc.Models.ListItem_Actions;
import com.abc.terry_sun.abc.R;
import com.abc.terry_sun.abc.Service.CardService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terry on 2015/5/14.
 */
public class Adapter_ActionList extends BaseAdapter {
    private Activity activity;
    private List<ListItem_Actions> stringPairList=new ArrayList<ListItem_Actions>();


    public Adapter_ActionList(Activity activity, List<Events> CardEventList) {
        super();
        this.activity = activity;
        for (Events item:CardEventList)
        {
            stringPairList.add(new ListItem_Actions(
                    CardService.getInstance().GetCardImageByCardID(item.getCardID()),
                    item.getEventTitle(),
                    item.getEventDescription()));
        }
    }
    public void UpdateData(List<Events> CardEventList)
    {
        stringPairList.clear();
        for (Events item:CardEventList)
        {
            stringPairList.add(new ListItem_Actions(
                    CardService.getInstance().GetCardImageByCardID(item.getCardID()),
                    item.getEventTitle(),
                    item.getEventDescription()));
        }
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

        ImageButton imageButton1 = (ImageButton) convertView.findViewById(R.id.imageButton);
        imageButton1.setImageBitmap(stringPairList.get(position).getItemImage());

        TextView ItemInfo1 = (TextView) convertView.findViewById(R.id.ItemInfo1);
        TextView ItemInfo2 = (TextView) convertView.findViewById(R.id.ItemInfo2);
        ItemInfo1.setText(((ListItem_Actions)stringPairList.get(position)).getTitle1());
        ItemInfo2.setText(((ListItem_Actions)stringPairList.get(position)).getTitle2());

        return convertView;
    }
}
