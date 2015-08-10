package com.abc.terry_sun.abc.Service;

import android.content.Context;
import android.util.Log;

import com.abc.terry_sun.abc.CustomClass.Application.ABCApplication;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Utilits.InternetUtil;
import com.abc.terry_sun.abc.Utilits.OkHttpUtil;
import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.ContentHandler;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.eclipse.agent.ExtensionMethodCompletionProposal;

/**
 * Created by terry_sun on 2015/7/24.
 */
public class ServerCommunicationService {
    String TAG="ServerCommunicationService";
    private static final ServerCommunicationService _ServerCommunicationService = new ServerCommunicationService();
    public static ServerCommunicationService getInstance() {
        return _ServerCommunicationService;
    }
    public void ChangeFavoriteCard(final String EntityCardID)
    {
        AsyncTaskHttpRequest _AsyncTaskHttpRequest = new AsyncTaskHttpRequest(
                MainActivity.GetMainActivityContext(),
                new AsyncTaskProcessingInterface() {
            @Override
            public void DoProcessing() {
                try {
                    List<BasicNameValuePair> UrlParams = new LinkedList<BasicNameValuePair>();
                    UrlParams.add(new BasicNameValuePair("UserFacebookID", VariableProvider.getInstance().getFacebookID()));
                    UrlParams.add(new BasicNameValuePair("EntityCardID", EntityCardID));
                    OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.ToggleFavoriteSetting, UrlParams));
                }
                catch (Exception ex)
                {
                    Log.e(TAG,ex.getMessage());
                }
            }
        });
        _AsyncTaskHttpRequest.execute();
    }

    public int AddNewCard(final String EntityCardID)
    {
        int result=0;//0:fail >0:CardID:
        try {
            List<BasicNameValuePair> UrlParams = new LinkedList<BasicNameValuePair>();
            UrlParams.add(new BasicNameValuePair("UserFacebookID", VariableProvider.getInstance().getFacebookID()));
            UrlParams.add(new BasicNameValuePair("EntityCardID", EntityCardID));
            result=OkHttpUtil.getIntFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.AddNewCard, UrlParams));
            if (result>0)
            {
                ServerCommunicationService.getInstance().GetUserCardInfo();
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG,ex.getMessage());
        }
        return result;
    }
    public String GetCardByEntityID(final String EntityCardID)
    {
        String CardID="";
        try {
            List<BasicNameValuePair> UrlParams = new LinkedList<BasicNameValuePair>();
            UrlParams.add(new BasicNameValuePair("UserFacebookID", VariableProvider.getInstance().getFacebookID()));
            UrlParams.add(new BasicNameValuePair("EntityCardID", EntityCardID));
            CardID=OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.GetUserCardIDByEntityID, UrlParams));
        }
        catch (Exception ex)
        {
            Log.e(TAG,ex.getMessage());
        }
        return CardID;
    }

    public void  GetUserCardInfo() {
        List<BasicNameValuePair> UrlParams= new LinkedList<BasicNameValuePair>();
        UrlParams.add(new BasicNameValuePair("UserFacebookID",VariableProvider.getInstance().getFacebookID()));
        Gson gson = new Gson();
        String UserCardsInJson="";
        try {
            UserCardsInJson = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.GetUserCard, UrlParams));
        }
        catch (IOException ex)
        {

        }
        Log.i("info","JsonArray.UserCardsInJson:"+ UserCardsInJson);
        Cards[] OnlineCardsArray = gson.fromJson(UserCardsInJson, Cards[].class);
        StorageService.GetAppStorageFolderInitial();
        Log.i("info", "OnlineCardsArray:"+OnlineCardsArray.length);
        String Result="";
        List<Cards> LocalCardList=CardService.getInstance().GetAllCards();

        //remove  item (sync with server)
        for(Cards OldItem:LocalCardList)
        {
            boolean IsNeedRemove=true;
            for (Cards Item:OnlineCardsArray)
            {
                if(Item.getCardID().equals(OldItem.getCardID()))
                {
                    IsNeedRemove=false;
                    break;
                }
            }
            if(IsNeedRemove) {
                OldItem.delete();
                OldItem.save();
            }
        }

        for (Cards Item:OnlineCardsArray)
        {
            //add & update
            List<Cards> CardList = Cards.find(Cards.class, "ENTITY_CARD_ID = ?", Item.getEntityCardID());
            if(CardList.size()==0)
            {
                //create
                Log.i("info","StorageService.GetImagePath(_context,Item.getCardImage()):"+ StorageService.GetImagePath(Item.getCardImage()));
                Item.setCreatedTimeFormated(StringService.GetJsonDate(Item.getCreatedTime()));
                Item.save();
                Result= InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation + ImageService.GetImageFileName(Item.getCategoryImage()), StorageService.GetImagePath(Item.getCategoryImage()));
                Result+=InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation+ImageService.GetImageFileName(Item.getGroupImage()), StorageService.GetImagePath(Item.getGroupImage()));
                Result+=InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation+ImageService.GetImageFileName(Item.getCardImage()), StorageService.GetImagePath(Item.getCardImage()));
                Log.i("info","StorageService.Result:"+ Result);
            }
            else
            {
                //update
                Cards _Cards=CardList.get(0);
                _Cards.UpdateInfo(Item);
                _Cards.save();
            }
        }

    }
}
