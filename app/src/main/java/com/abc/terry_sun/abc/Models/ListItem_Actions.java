package com.abc.terry_sun.abc.Models;

import android.graphics.Bitmap;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Terry on 2015/5/14.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class ListItem_Actions {
    private String EntityCardID;
    private Bitmap ItemImage;

    private DB_Cards CardInfo;
    private DB_Events EntityCardEvent;

    private DB_Events VirtualCardEvent;

    private String DirectPoint;
    private String IndirectPoint;
}
