package com.abc.terry_sun.abc.Models;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Terry on 2015/5/14.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ListItem_Actions {
    private Bitmap ItemImage;
    private String Title1;
    private String Title2;
}
