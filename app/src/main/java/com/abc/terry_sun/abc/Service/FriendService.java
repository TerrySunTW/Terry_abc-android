package com.abc.terry_sun.abc.Service;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.Models.CardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by terry_sun on 2015/8/31.
 */
public class FriendService {
    private static final FriendService _FriendService = new FriendService();
    public static FriendService getInstance() {
        return _FriendService;
    }
    public List<DB_Friend> GetAllFriends()
    {
        return DB_Friend.listAll(DB_Friend.class);
    }
}
