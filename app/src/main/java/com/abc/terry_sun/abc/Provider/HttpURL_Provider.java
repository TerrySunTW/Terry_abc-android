package com.abc.terry_sun.abc.Provider;

/**
 * Created by terry_sun on 2015/6/26.
 */
public class HttpURL_Provider {
    public static String ServerURL= "http://abc-web.azurewebsites.net/";
    public static final String ImageServerLocation= ServerURL + "CardImages/";
    public static final String FacebookLogin= ServerURL + "WebService/FacebookLogin";
    public static final String GetUserCard= ServerURL + "WebService/GetUserCard";
    public static final String GetUserFriends= ServerURL + "WebService/GetUserFriends";
    public static final String ToggleFavoriteSetting= ServerURL + "WebService/SetFavoriteCard";
    public static final String AddNewCard= ServerURL + "WebService/UserAddNewCard";
    public static final String AddFriendEntityCard= ServerURL + "WebService/AddFriendEntityCard";
    public static final String AddFriendEntityQRCard= ServerURL + "WebService/AddFriendEntityQRCard";
    public static final String AddFriendNFCCard= ServerURL + "WebService/AddFriendNFCCard";
    public static final String AddFriendRealCard= ServerURL + "WebService/UserAddFriendRealCard";
    public static final String GetUserCardIDByEntityID= ServerURL + "WebService/GetUserCardIDByEntityID";
    public static final String GetUserEvent= ServerURL + "WebService/GetUserEvent";
    public static final String SetMainCardSetting= ServerURL + "WebService/SetMainCard";
    public static final String ExchangeBonus= ServerURL + "WebService/ExchangeBonus";
    public static final String AddFriend= ServerURL + "WebService/AddFriend";

    public static final String AppVersion= ServerURL + "Version/GetVersion";
    public static final String AppURL= ServerURL + "Version/GetAPK";
}
