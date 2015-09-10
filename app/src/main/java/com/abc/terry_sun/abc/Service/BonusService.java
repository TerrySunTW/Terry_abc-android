package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.R;

import java.util.List;

/**
 * Created by terry_sun on 2015/8/20.
 */
public class BonusService {
    static Dialog BonusDialog;
    private static final BonusService _BonusService = new BonusService();
    public static BonusService getInstance() {
        return _BonusService;
    }
    public void ShowSingleBonusDialog(final String CardEventID)
    {
        Context context=MainActivity.GetMainActivityContext();

        final DB_Events _DB_Events=GetEventsByEventID(CardEventID);
        //EntityCardID
        String EntityCardID=CardService.getInstance().GetCardsByCardID(_DB_Events.getCardID()).getEntityCardID();
        BonusDialog=new Dialog(context);
        final DB_Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);


        BonusDialog.setContentView(R.layout.dialog_bonus_info);
        Window window = BonusDialog.getWindow();
        window.setLayout(ScreenService.GetScreenWidth(context).x - 100, ScreenService.GetScreenWidth(context).y - 300);



        Button Button_Exchange=(Button)BonusDialog.findViewById(R.id.Button_Exchange);
        Button_Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView TextView_BonusTitle=(TextView)BonusDialog.findViewById(R.id.TextView_BonusTitle);
        TextView_BonusTitle.setText(_DB_Events.getEventTitle());

        TextView TextView_BonusContent=(TextView)BonusDialog.findViewById(R.id.TextView_BonusContent);
        TextView_BonusContent.setText(_DB_Events.getEventDescription());

        BonusDialog.show();
    }
    public void ShowAllBonusDialog(final String EntityCardID)
    {
        Context context=MainActivity.GetMainActivityContext();


        //EntityCardID
        BonusDialog=new Dialog(context);
        final DB_Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);

        BonusDialog.setContentView(R.layout.dialog_all_bonus_info);
        Window window = BonusDialog.getWindow();
        window.setLayout(ScreenService.GetScreenWidth(context).x - 100, ScreenService.GetScreenWidth(context).y - 300);


        DB_Events EntityCardEvent= CardService.getInstance().GetEntityEventsByCardID(SelectedCardInfo.getCardID());
        if(EntityCardEvent!=null)
        {
            TextView TextView_BonusTitle=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus1Title);
            TextView_BonusTitle.setText(EntityCardEvent.getEventTitle());

            TextView TextView_BonusContent=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus1Content);
            TextView_BonusContent.setText(EntityCardEvent.getEventDescription());

            Button Button_Exchange=(Button)BonusDialog.findViewById(R.id.Button_Exchange1);
            Button_Exchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        DB_Events VirtualCardEvent=CardService.getInstance().GetVirtualEventsByCardID(SelectedCardInfo.getCardID());
        if(EntityCardEvent!=null)
        {
            TextView TextView_BonusTitle=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus2Title);
            TextView_BonusTitle.setText(VirtualCardEvent.getEventTitle());

            TextView TextView_BonusContent=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus2Content);
            TextView_BonusContent.setText(VirtualCardEvent.getEventDescription());

            Button Button_Exchange=(Button)BonusDialog.findViewById(R.id.Button_Exchange2);
            Button_Exchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }


        BonusDialog.show();
    }
    public List<DB_Events> GetEventsByCardID(String CardID)
    {
        List<DB_Events> DBEventsList = DB_Events.find(DB_Events.class, "CARD_ID=?", CardID);
        if(DBEventsList.size()>0)
        {
            return DBEventsList;
        }
        return null;
    }
    public DB_Events GetEventsByEventID(String EventID)
    {
        List<DB_Events> DBEventsList = DB_Events.find(DB_Events.class, "EVENT_ID=?", EventID);
        if(DBEventsList.size()>0)
        {
            return DBEventsList.get(0);
        }
        return null;
    }
}
