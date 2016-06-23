package com.abc.terry_sun.abc.CustomClass.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.terry_sun.abc.CustomClass.Application.ABCApplication;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Models.ListItem_Actions;
import com.abc.terry_sun.abc.R;
import com.abc.terry_sun.abc.Service.BonusService;
import com.abc.terry_sun.abc.Service.CardService;
import com.abc.terry_sun.abc.Service.StorageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terry on 2015/5/14.
 */
public class Adapter_BonusList extends BaseAdapter {
    private Activity activity;
    private List<ListItem_Actions> stringPairList=new ArrayList<ListItem_Actions>();
    final String TAG="Adapter_BonusList";

    public Adapter_BonusList(Activity activity) {
        super();
        this.activity = activity;
        //UpdateListItem(CardList);
    }

    private void UpdateListItem(List<DB_Cards> CardList) {
        Log.i(TAG, "CardList.size()="+ String.valueOf(CardList.size()));

        Log.i(TAG, "TempstringPairList is null");
        for (DB_Cards item : CardList) {
            ListItem_Actions _ListItem_Actions = new ListItem_Actions();
            _ListItem_Actions.setCardInfo(item);
            _ListItem_Actions.setItemImage(BitmapFactory.decodeFile(StorageService.GetImagePath(item.getCardImage())));
            _ListItem_Actions.setEntityCardID(item.getEntityCardID());
            _ListItem_Actions.setDirectPoint(item.getDirectPoint());
            _ListItem_Actions.setIndirectPoint(item.getIndirectPoint());
            _ListItem_Actions.setIsOwner(item.getHasRealCard());

            //get card bonus
            //Entity Event
            DB_Events EntityCardEvent = CardService.getInstance().GetEntityEventsByCardID(item.getCardID());
            _ListItem_Actions.setEntityCardEvent(EntityCardEvent);

            //Entity Event
            DB_Events VirtualCardEvent = CardService.getInstance().GetVirtualEventsByCardID(item.getCardID());
            _ListItem_Actions.setVirtualCardEvent(VirtualCardEvent);

            stringPairList.add(_ListItem_Actions);
        }
        stringPairList.add(null);

    }

    public void UpdateData(List<DB_Cards> CardList)
    {
        stringPairList.clear();
        UpdateListItem(CardList);
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
            convertView = inflater.inflate(R.layout.activity_bonus_item, null);
        }

        final ListItem_Actions _ListItem_Actions=stringPairList.get(position);
        if(_ListItem_Actions==null)
        {
            LinearLayout _LinearLayout = (LinearLayout) convertView.findViewById(R.id.ItemLinearLayout);
            _LinearLayout.setVisibility(View.INVISIBLE);
            return convertView;
        }
        final DB_Cards CardInfo =_ListItem_Actions.getCardInfo();
        final DB_Events EntityCardEvent= _ListItem_Actions.getEntityCardEvent();
        final DB_Events VirtualCardEvent= _ListItem_Actions.getVirtualCardEvent();

        LinearLayout _LinearLayout = (LinearLayout) convertView.findViewById(R.id.ItemLinearLayout);
        _LinearLayout.setVisibility(View.VISIBLE);

        ImageView CardButton = (ImageView) convertView.findViewById(R.id.CardButton);
        CardButton.setImageBitmap(_ListItem_Actions.getItemImage());
        CardButton.setTag(_ListItem_Actions.getEntityCardID());
        if(_ListItem_Actions.getIsOwner())
        {
            CardButton.setBackgroundColor(Color.YELLOW);
        }
        else
        {
            CardButton.setBackgroundColor(Color.BLACK);
        }
        CardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardService.getInstance().ShowCardDetailDialog(view.getTag().toString(),null, MainActivity.GetMainActivityContext());
            }
        });

        TextView Bonus1_Title = (TextView) convertView.findViewById(R.id.action1_title);
        TextView Bonus1_Title_Point = (TextView) convertView.findViewById(R.id.action1_title_point);
        TextView Bonus1_Title2 = (TextView) convertView.findViewById(R.id.action1_title2);
        TextView Bonus1_Content = (TextView) convertView.findViewById(R.id.action1_content);

        Bonus1_Title.setText(EntityCardEvent.getEventTitle()+"-DP:(");
        Bonus1_Title_Point.setText(_ListItem_Actions.getDirectPoint());
        Bonus1_Title2.setText("/"+EntityCardEvent.getDirectPointTarget()+")");

        Bonus1_Content.setText(EntityCardEvent.getEventDescription());


        TextView Bonus2_Title = (TextView) convertView.findViewById(R.id.action2_title);
        TextView Bonus2_Title_Point = (TextView) convertView.findViewById(R.id.action2_title_point);
        TextView Bonus2_Title2 = (TextView) convertView.findViewById(R.id.action2_title2);
        TextView Bonus2_Content = (TextView) convertView.findViewById(R.id.action2_content);


        Bonus2_Title.setText(VirtualCardEvent.getEventTitle() + "-IP:(");
        Bonus2_Title_Point.setText(_ListItem_Actions.getIndirectPoint());
        Bonus2_Title2.setText("/" + VirtualCardEvent.getIndirectPointTarget() + ")");

        Bonus2_Content.setText(VirtualCardEvent.getEventDescription());
        if(VirtualCardEvent==null) {
            Bonus2_Title.setVisibility(View.GONE);
            Bonus2_Content.setVisibility(View.GONE);
        }



        LinearLayout LinearLayout1 = (LinearLayout) convertView.findViewById(R.id.LinearLayout1);
        LinearLayout1.setTag(EntityCardEvent.getLink());
        LinearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BonusService.getInstance().ShowSingleBonusDialog(EntityCardEvent.getEventID(),_ListItem_Actions.getEntityCardID());

                String url = view.getTag().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                MainActivity.GetMainActivityContext().startActivity(i);
            }
        });

        LinearLayout LinearLayout2 = (LinearLayout) convertView.findViewById(R.id.LinearLayout2);
        LinearLayout2.setTag(VirtualCardEvent.getLink());
        LinearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BonusService.getInstance().ShowSingleBonusDialog(VirtualCardEvent.getEventID(),_ListItem_Actions.getEntityCardID());
                String url = view.getTag().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                MainActivity.GetMainActivityContext().startActivity(i);
            }
        });

        //exchange style
        if(Integer.parseInt(CardInfo.getDirectPoint())>=Integer.parseInt(EntityCardEvent.getDirectPointTarget())&&
                Integer.parseInt(CardInfo.getIndirectPoint())>=Integer.parseInt(EntityCardEvent.getIndirectPointTarget())) {
            LinearLayout1.setBackgroundColor(Color.GREEN);
        }

        if(Integer.parseInt(CardInfo.getDirectPoint())>=Integer.parseInt(VirtualCardEvent.getDirectPointTarget())&&
                Integer.parseInt(CardInfo.getIndirectPoint())>=Integer.parseInt(VirtualCardEvent.getIndirectPointTarget())) {
            LinearLayout2.setBackgroundColor(Color.GREEN);
        }

        if(EntityCardEvent.getHasExchanged()) {
            LinearLayout1.setBackgroundColor(Color.RED);
        }

        if(VirtualCardEvent.getHasExchanged()) {
            LinearLayout2.setBackgroundColor(Color.RED);
        }

        return convertView;
    }
}
