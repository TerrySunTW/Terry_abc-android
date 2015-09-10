package com.abc.terry_sun.abc.CustomClass.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    public Adapter_BonusList(Activity activity, List<DB_Cards> CardList) {
        super();
        this.activity = activity;
        UpdateListItem(CardList);
    }

    private void UpdateListItem(List<DB_Cards> CardList) {
        for (DB_Cards item:CardList)
        {
            ListItem_Actions _ListItem_Actions=new ListItem_Actions();
            _ListItem_Actions.setItemImage(BitmapFactory.decodeFile(StorageService.GetImagePath(item.getCardImage())));
            _ListItem_Actions.setEntityCardID(item.getEntityCardID());
            _ListItem_Actions.setDirectPoint(item.getDirectPoint());
            _ListItem_Actions.setIndirectPoint(item.getIndirectPoint());



            //get card bonus
            //Entity Event
            DB_Events EntityCardEvent= CardService.getInstance().GetEntityEventsByCardID(item.getCardID());
            if(EntityCardEvent!=null)
            {
                _ListItem_Actions.setBonus1_EventID(EntityCardEvent.getEventID());
                _ListItem_Actions.setBonus1_Title(EntityCardEvent.getEventTitle());
                _ListItem_Actions.setBonus1_Description(EntityCardEvent.getEventDescription());
                _ListItem_Actions.setBonus1_CardType(EntityCardEvent.getCardType());
                _ListItem_Actions.setDirectPointTarget1(EntityCardEvent.getDirectPointTarget());
                _ListItem_Actions.setIndirectPointTarget1(EntityCardEvent.getIndirectPointTarget());
            }
            //Entity Event
            DB_Events VirtualCardEvent=CardService.getInstance().GetVirtualEventsByCardID(item.getCardID());
            if(VirtualCardEvent!=null)
            {
                _ListItem_Actions.setBonus2_EventID(VirtualCardEvent.getEventID());
                _ListItem_Actions.setBonus2_Title(VirtualCardEvent.getEventTitle());
                _ListItem_Actions.setBonus2_Description(VirtualCardEvent.getEventDescription());
                _ListItem_Actions.setBonus2_CardType(VirtualCardEvent.getCardType());
                _ListItem_Actions.setDirectPointTarget2(VirtualCardEvent.getDirectPointTarget());
                _ListItem_Actions.setIndirectPointTarget2(VirtualCardEvent.getIndirectPointTarget());
            }

            stringPairList.add(_ListItem_Actions);
        }
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

        ListItem_Actions _ListItem_Actions=stringPairList.get(position);
        ImageButton CardButton = (ImageButton) convertView.findViewById(R.id.CardButton);
        CardButton.setImageBitmap(_ListItem_Actions.getItemImage());
        CardButton.setTag(_ListItem_Actions.getEntityCardID());
        CardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardService.getInstance().ShowCardDetailDialog(view.getTag().toString(), MainActivity.GetMainActivityContext());
            }
        });

        TextView Bonus1_Title = (TextView) convertView.findViewById(R.id.action1_title);
        TextView Bonus1_Content = (TextView) convertView.findViewById(R.id.action1_content);

        Bonus1_Title.setText(_ListItem_Actions.getBonus1_Title()+" -DP:("+_ListItem_Actions.getDirectPoint()+"/"+_ListItem_Actions.getDirectPointTarget1()+")");
        Bonus1_Content.setText(_ListItem_Actions.getBonus1_Description());


        TextView Bonus2_Title = (TextView) convertView.findViewById(R.id.action2_title);
        TextView Bonus2_Content = (TextView) convertView.findViewById(R.id.action2_content);


        Bonus2_Title.setText(_ListItem_Actions.getBonus2_Title() + "-IP:(" + _ListItem_Actions.getIndirectPoint() + "/" + _ListItem_Actions.getIndirectPointTarget2() + ")");
        Bonus2_Content.setText(_ListItem_Actions.getBonus2_Description());
        if(_ListItem_Actions.getBonus2_Title()==null||_ListItem_Actions.getIndirectPointTarget2()==null) {
            Bonus2_Title.setVisibility(View.GONE);
            Bonus2_Content.setVisibility(View.GONE);
        }



        LinearLayout LinearLayout1 = (LinearLayout) convertView.findViewById(R.id.LinearLayout1);
        LinearLayout1.setTag(_ListItem_Actions.getBonus1_EventID());
        LinearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BonusService.getInstance().ShowSingleBonusDialog(view.getTag().toString());
            }
        });

        LinearLayout LinearLayout2 = (LinearLayout) convertView.findViewById(R.id.LinearLayout2);
        LinearLayout2.setTag(_ListItem_Actions.getBonus2_EventID());
        LinearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BonusService.getInstance().ShowSingleBonusDialog(view.getTag().toString());
            }
        });


        return convertView;
    }
}
