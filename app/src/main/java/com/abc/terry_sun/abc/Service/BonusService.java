package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.Entities.Events;
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
        final Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);
        final Events _Events=GetEventsByCardID(SelectedCardInfo.getCardID());

        BonusDialog.setContentView(R.layout.dialog_bonus_info);
        Window window = BonusDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
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
        TextView_BonusTitle.setText(_Events.getEventTitle());

        TextView TextView_BonusContent=(TextView)BonusDialog.findViewById(R.id.TextView_BonusContent);
        TextView_BonusContent.setText(_Events.getEventDescription());

        BonusDialog.show();
    }
    public Events GetEventsByCardID(String CardID)
    {
        List<Events> EventsList=Events.find(Events.class, "CARD_ID=?", CardID);
        if(EventsList.size()>0)
        {
            return EventsList.get(0);
        }
        return null;
    }
}
