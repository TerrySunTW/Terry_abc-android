package com.abc.terry_sun.abc.Provider;

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
    private Boolean NFC_Enable=false;

    public String GetLocation() {
        if (LastLatitude != null && LastLongitude != null) {
            return LastLatitude + "," + LastLongitude;
        }
        return "";
    }

    public int ExchangeResult = 0;
    //UserCardID
    public String LastNFCKey;


    public boolean CheckLastNFCKeyIsNotNull()
    {
        return LastNFCKey!=null;
    }
}