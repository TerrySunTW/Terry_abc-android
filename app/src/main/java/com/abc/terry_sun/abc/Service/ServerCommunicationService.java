package com.abc.terry_sun.abc.Service;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.Models.BaseReturnModel;
import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Utilits.InternetUtil;
import com.abc.terry_sun.abc.Utilits.OkHttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

/**
 * Created by terry_sun on 2015/7/24.
 */
public class ServerCommunicationService {
    static String TAG="ServerCommunicationService";
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
                    ContentValues UrlParams = new ContentValues();
                    UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
                    UrlParams.put("EntityCardID", EntityCardID);
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
                            ContentValues UrlParams = new ContentValues();
                            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
                            UrlParams.put("EntityCardID", EntityCardID);
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
    public void RemoveUserCard(final String EntityCardID)
    {
        AsyncTaskHttpRequest _AsyncTaskHttpRequest = new AsyncTaskHttpRequest(
                MainActivity.GetMainActivityContext(),
                new AsyncTaskProcessingInterface() {
                    @Override
                    public void DoProcessing() {
                        try {
                            ContentValues UrlParams = new ContentValues();
                            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
                            UrlParams.put("EntityCardID", EntityCardID);
                            OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.RemoveUserCard, UrlParams));
                        }
                        catch (Exception ex)
                        {
                            Log.e(TAG,ex.getMessage());
                        }
                    }
                });
        _AsyncTaskHttpRequest.execute();
    }

    //CARD QR
    public int AddNewCard(final String CardSN)
    {
        int result=0;//0:fail >0:CardID:
        try {
            ContentValues UrlParams = new ContentValues();
            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
            UrlParams.put("CardSN", CardSN);
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
    //for NFC&QR
    public int AddFriendEntityCard(final String ReadUserCardID,final String ReadCardID)
    {
        int result=0;//0:fail >0:CardID:
        try {
            ContentValues UrlParams = new ContentValues();
            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
            UrlParams.put("ReadUserCardID", ReadUserCardID);
            UrlParams.put("ReadCardID", ReadCardID);

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

    //only NFC
    public int AddFriendNFCCard(final String NFCCardID)
    {
        int result=0;//0:fail >0:CardID:
        try {
            ContentValues UrlParams = new ContentValues();
            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
            UrlParams.put("NFC_UserCardID", NFCCardID);
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
            ContentValues UrlParams = new ContentValues();
            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
            UrlParams.put("EntityCardID", EntityCardID);
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
    public void UpdateFriendInfo()
    {
        GetUserFriendInfo();
    }
    public void  GetUserCardInfo() {
        ContentValues UrlParams= new ContentValues();
        UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
        Gson gson = new Gson();
        String UserCardsInJson="";
        try {
            UserCardsInJson = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.GetUserCard, UrlParams));
        }
        catch (IOException ex)
        {

        }
        //Log.i("info","JsonArray.UserCardsInJson:"+ UserCardsInJson);
        DB_Cards[] onlineDBCardsArray = gson.fromJson(UserCardsInJson, DB_Cards[].class);
        StorageService.GetAppStorageFolderInitial();
        //Log.i("info", "OnlineCardsArray:"+ onlineDBCardsArray.length);
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
            }
        }

        for (DB_Cards Item: onlineDBCardsArray)
        {
            //add & update
            List<DB_Cards> CardList = DB_Cards.find(DB_Cards.class, "ENTITY_CARD_ID = ? and GROUP_ID=?", Item.getEntityCardID(),Item.getGroupID());
            if(CardList.size()==0)
            {
                //create
                Log.i("info","StorageService.GetImagePath(_context,Item.getCardImage()):"+ StorageService.GetImagePath(Item.getCardImage()));
                Item.setCreatedTimeFormated(StringService.GetJsonDate(Item.getCreatedTime()));
                Item.save();
                Result+=InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation+ImageService.GetImageFileName(Item.getCardImage()), StorageService.GetImagePath(Item.getCardImage()));

                Log.i(TAG,"StorageService.URL:"+ HttpURL_Provider.ImageServerLocation+ImageService.GetImageFileName(Item.getCardImage()));
                Log.i(TAG,"StorageService.LocalPath:"+ StorageService.GetImagePath(Item.getCardImage()));
                Log.i(TAG,"StorageService.Result:"+ Result);
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
        ContentValues UrlParams= new ContentValues();
        UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
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
            Item.setEndDateFormated(StringService.GetJsonDate(Item.getEndDate()));
            Item.save();
        }
    }

    public void  GetUserFriendInfo() {
        ContentValues UrlParams= new ContentValues();
        UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
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

        if(online_Friend!=null) {
            for (DB_Friend Item : online_Friend) {
                DownLoadFriendImage(Item.getFriendImage(), Item.getFriendID());
                Item.save();
            }
        }
    }

    public static void DownLoadFriendImage(String _URL,String FriendID)
    {
        try {
            InternetUtil.DownloadFacebookProfilePictureFile(_URL, StorageService.GetFriendImagePath(FriendID));
        }
        catch (Exception ex)
        {
            Log.e(TAG,ex.getStackTrace().toString());
        }
    }
    public BaseReturnModel ExchangeBonus(final DB_Events _DB_Events,final DB_Cards _DB_Cards)
    {
        BaseReturnModel _BaseReturnModel=new BaseReturnModel();
        Gson gson = new Gson();
        try {
            ContentValues UrlParams = new ContentValues();
            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
            UrlParams.put("EntityCardID", _DB_Cards.getEntityCardID());
            UrlParams.put("CardID", _DB_Events.getCardID());
            UrlParams.put("EventID", _DB_Events.getEventID());
            UrlParams.put("Location", VariableProvider.getInstance().GetLocation());
            String JsonData=OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.ExchangeBonus, UrlParams));
            _BaseReturnModel = gson.fromJson(JsonData, BaseReturnModel.class);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
        return _BaseReturnModel;
    }
    public String AddFriend(final String FriendID)
    {
        String CardID="";
        try {
            ContentValues UrlParams = new ContentValues();
            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
            UrlParams.put("FriendID", FriendID);
            CardID=OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.AddFriend, UrlParams));
        }
        catch (Exception ex)
        {
            Log.e(TAG,ex.getMessage());
        }
        return CardID;
    }
    public boolean IsVersionSameWithServer(Context context)
    {
        String ServerVersion="";
        try
        {
            ServerVersion= OkHttpUtil.getStringFromServer(HttpURL_Provider.AppVersion);
        }
        catch (Exception ex)
        {
            Log.e(TAG,ex.getMessage());
        }
        String AppVersion="";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            AppVersion= pInfo.versionName;
        }
        catch(Exception ex) {

        }
        return AppVersion.equals(ServerVersion);
    }
    public void RemoveUserFriend(final String FriendID)
    {
        AsyncTaskHttpRequest _AsyncTaskHttpRequest = new AsyncTaskHttpRequest(
                MainActivity.GetMainActivityContext(),
                new AsyncTaskProcessingInterface() {
                    @Override
                    public void DoProcessing() {
                        try {
                            ContentValues UrlParams = new ContentValues();
                            UrlParams.put("UserFacebookID", VariableProvider.getInstance().getFacebookID());
                            UrlParams.put("FriendID", FriendID);
                            OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.RemoveFriend, UrlParams));
                        }
                        catch (Exception ex)
                        {
                            Log.e(TAG,ex.getMessage());
                        }
                    }
                });
        _AsyncTaskHttpRequest.execute();
    }
}
