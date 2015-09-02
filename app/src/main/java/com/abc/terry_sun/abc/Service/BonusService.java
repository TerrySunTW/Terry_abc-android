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
    public void ShowBonusDialog(final String EntityCardID)
    {
        Context context=MainActivity.GetMainActivityContext();
        BonusDialog=new Dialog(context);
        final DB_Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);
        final DB_Events _DB_Events =GetEventsByCardID(SelectedCardInfo.getCardID());

        BonusDialog.setContentView(R.layout.dialog_bonus_info);
        Window window = BonusDialog.getWindow();
        //window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ScreenService.GetScreenWidth(context).x - 100, ScreenService.GetScreenWidth(context).y - 300);


        Button Button_ActionLink=(Button)BonusDialog.findViewById(R.id.Button_ActionLink);
        Button_ActionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
    public DB_Events GetEventsByCardID(String CardID)
    {
        List<DB_Events> DBEventsList = DB_Events.find(DB_Events.class, "CARD_ID=?", CardID);
        if(DBEventsList.size()>0)
        {
            return DBEventsList.get(0);
        }
        return null;
    }
}
