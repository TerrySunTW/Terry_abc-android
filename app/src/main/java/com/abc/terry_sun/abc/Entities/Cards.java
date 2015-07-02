package com.abc.terry_sun.abc.Entities;

import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.Date;

import lombok.Data;

/**
 * Created by terry_sun on 2015/6/29.
 */
@Data
public class Cards extends SugarRecord<Cards> {

    String CategoryID;
    String CategoryName;
    String CategoryImage;
    String CategoryVersion;

    String GroupID;
    String GroupName;
    String GroupImage;
    String GroupVersion;

    String RepresentativeID;
    String RepresentativeName;
    String RepresentativeImage;
    String RepresentativeVersion;

    String CardID;
    String CardName;
    String CardImage;
    String CardDescription;
    String CardVersion;

    String EntityCardID;
    String SerialNumber;
    String UserCardID;
    String IntroducerID;
    String OwnerID;
    String CardLevel;
    Boolean IsFavorite;
    Boolean IsMainCard;
    String CreatedTime;
    Date CreatedTimeFormated;
}
