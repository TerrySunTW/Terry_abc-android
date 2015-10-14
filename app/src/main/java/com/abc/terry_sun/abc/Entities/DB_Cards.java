package com.abc.terry_sun.abc.Entities;

import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.StorageService;
import com.abc.terry_sun.abc.Utilits.InternetUtil;
import com.orm.SugarRecord;

import java.util.Date;

import lombok.Data;

/**
 * Created by terry_sun on 2015/6/29.
 */
@Data
public class DB_Cards extends SugarRecord<DB_Cards> {

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
    Boolean IsFavorite;
    Boolean IsMainCard;
    String RelationLink;
    String CreatedTime;
    String DirectPoint;
    String IndirectPoint;
    Date CreatedTimeFormated;
    Boolean HasRealCard;
    public void UpdateInfo(DB_Cards NewInfo)
    {
        this.setRelationLink(NewInfo.getRelationLink());
        this.setIsFavorite(NewInfo.getIsFavorite());
        this.setIsMainCard(NewInfo.getIsMainCard());
        this.setCardDescription(NewInfo.getCardDescription());

        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation + ImageService.GetImageFileName(NewInfo.getCategoryImage()), StorageService.GetImagePath(ImageService.GetImageFileName(NewInfo.getCategoryImage())));
        this.setCategoryName(NewInfo.getCategoryName());
        this.setCategoryImage(NewInfo.getCategoryImage());
        this.setCategoryVersion(NewInfo.getCategoryVersion());

        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation + ImageService.GetImageFileName(NewInfo.getGroupImage()), StorageService.GetImagePath(ImageService.GetImageFileName(NewInfo.getGroupImage())));
        this.setGroupName(NewInfo.getGroupName());
        this.setGroupImage(NewInfo.getGroupImage());
        this.setGroupVersion(NewInfo.getGroupVersion());

        //update to
        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation + ImageService.GetImageFileName(NewInfo.getRepresentativeImage()), StorageService.GetImagePath(ImageService.GetImageFileName(NewInfo.getRepresentativeImage())));
        this.setRepresentativeName(NewInfo.getRepresentativeName());
        this.setRepresentativeImage(NewInfo.getRepresentativeImage());
        this.setRepresentativeVersion(NewInfo.getRepresentativeVersion());

        //update to
        InternetUtil.DownloadFile(HttpURL_Provider.ImageServerLocation + ImageService.GetImageFileName(NewInfo.getCardImage()), StorageService.GetImagePath(ImageService.GetImageFileName(NewInfo.getCardImage())));
        this.setCardName(NewInfo.getCardName());
        this.setCardImage(NewInfo.getCardImage());
        this.setCardDescription(NewInfo.getCardDescription());
        this.setCardVersion(NewInfo.getCardVersion());

        this.setDirectPoint(NewInfo.getDirectPoint());
        this.setIndirectPoint(NewInfo.getIndirectPoint());

        this.setHasRealCard(NewInfo.getHasRealCard());

    }
}
