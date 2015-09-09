package com.abc.terry_sun.abc.Models;

import android.graphics.Bitmap;

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
    private String Bonus1_EventID;
    private String Bonus1_Title;
    private String Bonus1_Description;
    private String Bonus1_CardType;
    private String DirectPointTarget1;
    private String IndirectPointTarget1;

    private String Bonus2_EventID;
    private String Bonus2_Title;
    private String Bonus2_Description;
    private String Bonus2_CardType;
    private String DirectPointTarget2;
    private String IndirectPointTarget2;

    private String DirectPoint;
    private String IndirectPoint;
}
