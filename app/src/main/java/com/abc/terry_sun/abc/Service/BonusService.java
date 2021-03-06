package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.terry_sun.abc.BonusListActivity;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Models.BaseReturnModel;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.R;

import java.util.Date;
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

    public static String GetBonusLeftDayString(DB_Events _DB_Events) {
        Date CurrentTime=new Date();
        long diff = Math.abs(_DB_Events.getEndDateFormated().getTime() - CurrentTime.getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return String.valueOf(diffDays);
    }
    public void ShowSingleBonusDialog(final String CardEventID,final String EntityCardID)
    {
        Context context=MainActivity.GetMainActivityContext();

        final DB_Events _DB_Events=GetEventsByEventID(CardEventID);
        //EntityCardID
        BonusDialog=new Dialog(context);
        final DB_Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);


        BonusDialog.setContentView(R.layout.dialog_bonus_info);
        Window window = BonusDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ScreenService.GetScreenWidth(), ScreenService.GetScreenHeight());

        TextView TextView_BonusLeftDay=(TextView)BonusDialog.findViewById(R.id.TextView_BonusLeftDay);
        TextView_BonusLeftDay.setText(BonusService.GetBonusLeftDayString(_DB_Events) + " days left!!");

        final Button Button_Exchange=(Button)BonusDialog.findViewById(R.id.Button_Exchange);
        Button_Exchange.setVisibility(View.GONE);
        if(
            Integer.parseInt(SelectedCardInfo.getDirectPoint())>=Integer.parseInt(_DB_Events.getDirectPointTarget()) &&
            Integer.parseInt(SelectedCardInfo.getIndirectPoint())>=Integer.parseInt(_DB_Events.getIndirectPointTarget())
                //&&????????????
                )
        {
            Button_Exchange.setVisibility(View.VISIBLE);
            TextView_BonusLeftDay.setVisibility(View.GONE);
        }
        if(_DB_Events.getHasExchanged())
        {
            Button_Exchange.setEnabled(false);
            Button_Exchange.setText("?????????");
        }
        Button_Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GPSService mGPS = new GPSService(MainActivity.GetMainActivityContext());
                if (mGPS.canGetLocation) {
                    mGPS.getLocation();
                    VariableProvider.getInstance().setLastLatitude(String.valueOf(mGPS.getLatitude()));
                    VariableProvider.getInstance().setLastLongitude(String.valueOf(mGPS.getLongitude()));
                }
                AsyncTaskHttpRequest _AsyncTaskHttpRequest = new AsyncTaskHttpRequest(MainActivity.GetMainActivityContext(),
                        new AsyncTaskProcessingInterface() {
                            @Override
                            public void DoProcessing() {
                                VariableProvider.getInstance().setExchangeResult(0);
                                BaseReturnModel _BaseReturnModel = ServerCommunicationService.getInstance().ExchangeBonus(_DB_Events, SelectedCardInfo);
                                if (_BaseReturnModel.getCode() > 0) {
                                    VariableProvider.getInstance().setExchangeResult(_BaseReturnModel.getCode());
                                    ServerCommunicationService.getInstance().UpdateServerInfo();
                                }
                            }
                        },
                        new AsyncTaskPostProcessingInterface() {
                            @Override
                            public void DoProcessing() {
                                if(VariableProvider.getInstance().getExchangeResult()>0)
                                {
                                    ProcessControlService.AlertMessage(MainActivity.GetMainActivityContext(),"????????????!!");
                                    Button_Exchange.setText("?????????");
                                    Button_Exchange.setEnabled(false);
                                }
                                else
                                {
                                    ProcessControlService.AlertMessage(MainActivity.GetMainActivityContext(),"????????????!!");
                                }
                            }
                        }
                );
                _AsyncTaskHttpRequest.execute();

            }
        });

        LinearLayout LinearLayout_Background =(LinearLayout)BonusDialog.findViewById(R.id.LinearLayout_Background);
        LinearLayout_Background.setBackground(new BitmapDrawable(CardService.getInstance().GetCardImageByCardID(SelectedCardInfo.getCardID())));
        int CardWidth = ScreenService.GetScreenWidth();
        int CardHeight=(CardWidth/5)*7;
        LinearLayout_Background.getLayoutParams().height = CardHeight;
        LinearLayout_Background.requestLayout();

        TextView TextView_BonusTitle=(TextView)BonusDialog.findViewById(R.id.TextView_BonusTitle);
        TextView_BonusTitle.setText(_DB_Events.getEventTitle());

        TextView TextView_BonusContent=(TextView)BonusDialog.findViewById(R.id.TextView_BonusContent);
        TextView_BonusContent.setText(_DB_Events.getEventDescription());

        TextView TextView_BonusLink=(TextView)BonusDialog.findViewById(R.id.TextView_BonusLink);
        TextView_BonusLink.setTag(_DB_Events.getLink());
        TextView_BonusLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = v.getTag().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                MainActivity.GetMainActivityContext().startActivity(i);
            }
        });

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
        window.setLayout(ScreenService.GetScreenWidth(), ScreenService.GetScreenHeight());


        final DB_Events EntityCardEvent= CardService.getInstance().GetEntityEventsByCardID(SelectedCardInfo.getCardID());
        if(EntityCardEvent!=null)
        {
            TextView TextView_BonusTitle=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus1Title);
            TextView_BonusTitle.setText(EntityCardEvent.getEventTitle());

            TextView TextView_BonusContent=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus1Content);
            TextView_BonusContent.setText(EntityCardEvent.getEventDescription());

            Button Button_View1=(Button)BonusDialog.findViewById(R.id.Button_View1);
            Button_View1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BonusDialog.dismiss();
                    BonusService.getInstance().ShowSingleBonusDialog(EntityCardEvent.getEventID(),SelectedCardInfo.getEntityCardID());
                }
            });
        }

        final DB_Events VirtualCardEvent=CardService.getInstance().GetVirtualEventsByCardID(SelectedCardInfo.getCardID());
        if(EntityCardEvent!=null)
        {
            TextView TextView_BonusTitle=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus2Title);
            TextView_BonusTitle.setText(VirtualCardEvent.getEventTitle());

            TextView TextView_BonusContent=(TextView)BonusDialog.findViewById(R.id.TextView_Bonus2Content);
            TextView_BonusContent.setText(VirtualCardEvent.getEventDescription());

            Button Button_View2=(Button)BonusDialog.findViewById(R.id.Button_View2);
            Button_View2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BonusDialog.dismiss();
                    BonusService.getInstance().ShowSingleBonusDialog(VirtualCardEvent.getEventID(), SelectedCardInfo.getEntityCardID());
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
