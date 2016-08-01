package com.abc.terry_sun.abc.Provider;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.abc.terry_sun.abc.BonusListActivity;

import lombok.Data;

/**
 * Created by terry_sun on 2015/4/22.
 */
@Data
public final class VariableProvider {
    private static final VariableProvider _VariableProvider = new VariableProvider();
    public static VariableProvider getInstance() {
        return _VariableProvider;
    }
    private boolean IsRuningServiceThread;

    private String FacebookID;
    private String FacebookUserName;
    private String FacebookLink;
    private String FacebookPhotoURL;
    private String LastLatitude;
    private String LastLongitude;


    public String GetLocation() {
        if (LastLatitude != null && LastLongitude != null) {
            return LastLatitude + "," + LastLongitude;
        }
        return "";
    }

    public int ExchangeResult = 0;
    //UserCardID
    public String LastNFCKey;
    public String getLastNFCKey()
    {
        String TempLastNFCKey=LastNFCKey;
        LastNFCKey=null;
        return TempLastNFCKey;
    }

    public boolean CheckLastNFCKeyIsNotNull()
    {
        return LastNFCKey!=null;
    }

    public static String GetVersion(Context context)
    {
        String AppVersion="";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            AppVersion= pInfo.versionName;
        }
        catch(Exception ex) {

        }
        return AppVersion;
    }
}