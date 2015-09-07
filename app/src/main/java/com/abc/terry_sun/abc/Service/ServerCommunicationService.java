package com.abc.terry_sun.abc.Service;

import android.os.Environment;
import android.util.Log;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Utilits.InternetUtil;
import com.abc.terry_sun.abc.Utilits.OkHttpUtil;
import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

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
    public void SetMainCard(final String EntityCardID)
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
                            OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.SetMainCardSetting, UrlParams));
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
    public int AddFriendEntityCard(final String EntityCardID)
    {
        int result=0;//0:fail >0:CardID:
        try {
            List<BasicNameValuePair> UrlParams = new LinkedList<BasicNameValuePair>();
            UrlParams.add(new BasicNameValuePair("UserFacebookID", VariableProvider.getInstance().getFacebookID()));
            UrlParams.add(new BasicNameValuePair("EntityCardID", EntityCardID));
            result=OkHttpUtil.getIntFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.AddFriendEntityCard, UrlParams));
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
    public int AddFriendNFCCard(final String NFCCardID)
    {
        int result=0;//0:fail >0:CardID:
        try {
            List<BasicNameValuePair> UrlParams = new LinkedList<BasicNameValuePair>();
            UrlParams.add(new BasicNameValuePair("UserFacebookID", VariableProvider.getInstance().getFacebookID()));
            UrlParams.add(new BasicNameValuePair("NFCCardID", NFCCardID));
            result=OkHttpUtil.getIntFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.AddFriendNFCCard, UrlParams));
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
    public void UpdateServerInfo()
    {
        GetUserCardInfo();
        GetUserEventInfo();
        GetUserFriendInfo();
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
        DB_Cards[] onlineDBCardsArray = gson.fromJson(UserCardsInJson, DB_Cards[].class);
        StorageService.GetAppStorageFolderInitial();
        Log.i("info", "OnlineCardsArray:"+ onlineDBCardsArray.length);
        String Result="";
        List<DB_Cards> LocalCardList=CardService.getInstance().GetAllCards();

        //remove  item (sync with server)
        for(DB_Cards OldItem:LocalCardList)
        {
            boolean IsNeedRemove=true;
            for (DB_Cards Item: onlineDBCardsArray)
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

        for (DB_Cards Item: onlineDBCardsArray)
        {
            //add & update
            List<DB_Cards> CardList = DB_Cards.find(DB_Cards.class, "ENTITY_CARD_ID = ?", Item.getEntityCardID());
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
                DB_Cards _DB_Cards =CardList.get(0);
                _DB_Cards.UpdateInfo(Item);
                _DB_Cards.save();
            }
        }
    }
    public void  GetUserEventInfo() {
        List<BasicNameValuePair> UrlParams= new LinkedList<BasicNameValuePair>();
        UrlParams.add(new BasicNameValuePair("UserFacebookID",VariableProvider.getInstance().getFacebookID()));
        Gson gson = new Gson();
        String JsonData="";
        try {
            JsonData = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.GetUserEvent, UrlParams));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        DB_Events[] onlineEvents = gson.fromJson(JsonData, DB_Events[].class);

        CardService.getInstance().RemoveAllEvents();


        for (DB_Events Item: onlineEvents)
        {
            Item.save();
        }
    }

    public void  GetUserFriendInfo() {
        List<BasicNameValuePair> UrlParams= new LinkedList<BasicNameValuePair>();
        UrlParams.add(new BasicNameValuePair("UserFacebookID",VariableProvider.getInstance().getFacebookID()));
        Gson gson = new Gson();
        String JsonData="";
        try {
            JsonData = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.GetUserFriends, UrlParams));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        DB_Friend[] online_Friend = gson.fromJson(JsonData, DB_Friend[].class);

        CardService.getInstance().RemoveAllFriends();


        for (DB_Friend Item: online_Friend)
        {
            DownLoadFriendImage(Item.getFriendImage(),Item.getFriendID());
            Item.save();
        }
    }

    public static void DownLoadFriendImage(String _URL,String FriendID)
    {
        try {
            InternetUtil.DownloadFacebookProfilePictureFile(_URL, StorageService.GetFriendImagePath(FriendID));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
