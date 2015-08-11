package com.abc.terry_sun.abc.Entities;

import android.content.Context;
import android.util.Log;

import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.StorageService;
import com.abc.terry_sun.abc.Utilits.InternetUtil;
import com.orm.SugarRecord;

import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.Date;

import lombok.Data;

/**
 * Created by terry_sun on 2015/6/29.
 */
@Data
public class Events extends SugarRecord<Events> {
    String CardID;
    String CategoryID;
    String GroupID;
    String RepresentativeID;
    String EventID;
    String EventTitle;
    String EventDescription;
}
