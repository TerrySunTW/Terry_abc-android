package com.abc.terry_sun.abc.Models;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Terry on 2015/5/14.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ListItem_Friend {
    private String FriendID;
    private Bitmap FriendImage;
    private String Name;
    private String CardCount;
}
