package com.abc.terry_sun.abc.Entities;

import com.orm.SugarRecord;

import lombok.Data;

/**
 * Created by terry_sun on 2015/8/31.
 */

@Data
public class DB_Friend extends SugarRecord<DB_Friend> {
    String FriendID;
    String FriendName;
    String FriendImage;
    String CardCount;
}
