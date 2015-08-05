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
    String RelationLink;
    String CreatedTime;
    Date CreatedTimeFormated;
    public void UpdateInfo(Cards NewInfo)
    {
        this.setRelationLink(NewInfo.getRelationLink());
        this.setIsFavorite(NewInfo.getIsFavorite());
        this.setIsMainCard(NewInfo.getIsMainCard());
        this.setCardDescription(NewInfo.getCardDescription());

        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation + ImageService.GetImageFileName(NewInfo.getCategoryImage()), StorageService.GetImagePath(ImageService.GetImageFileName(NewInfo.getCategoryImage())));
        this.setCategoryName(NewInfo.getCategoryName());
        this.setCategoryImage(NewInfo.getCategoryImage());
        this.setCategoryVersion(NewInfo.getCategoryVersion());

        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation+ImageService.GetImageFileName(NewInfo.getGroupImage()), StorageService.GetImagePath(ImageService.GetImageFileName(NewInfo.getGroupImage())));
        this.setGroupName(NewInfo.getGroupName());
        this.setGroupImage(NewInfo.getGroupImage());
        this.setGroupVersion(NewInfo.getGroupVersion());

        //update to
        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation+ImageService.GetImageFileName(NewInfo.getRepresentativeImage()), StorageService.GetImagePath(ImageService.GetImageFileName(NewInfo.getRepresentativeImage())));
        this.setRepresentativeName(NewInfo.getRepresentativeName());
        this.setRepresentativeImage(NewInfo.getRepresentativeImage());
        this.setRepresentativeVersion(NewInfo.getRepresentativeVersion());

        //update to
        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation+ImageService.GetImageFileName(NewInfo.getCardImage()), StorageService.GetImagePath( ImageService.GetImageFileName(NewInfo.getCardImage())));
        this.setCardName(NewInfo.getCardName());
        this.setCardImage(NewInfo.getCardImage());
        this.setCardDescription(NewInfo.getCardDescription());
        this.setCardVersion(NewInfo.getCardVersion());

    }
}
