package com.abc.terry_sun.abc.Entities;

import com.orm.SugarRecord;

import java.util.Date;

import lombok.Data;

/**
 * Created by terry_sun on 2015/6/29.
 */

@Data
public class DB_Events extends SugarRecord {
    public DB_Events() {}
    String CardID;
    String CategoryID;
    String GroupID;
    String RepresentativeID;
    String CardType;
    String EventID;
    String EventTitle;
    String EventDescription;
    String DirectPointTarget;
    String IndirectPointTarget;
    Boolean HasExchanged;
    String Link;
    String EndDate;
    Date EndDateFormated;
}
