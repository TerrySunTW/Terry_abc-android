package com.abc.terry_sun.abc;

import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.abc.terry_sun.abc.Service.ProcessControlService;

import java.util.ArrayList;

/**
 * Created by terry_sun on 2015/6/2.
 */
public class TabGroup_Link  extends BaseTabGroup {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirstActivity=FriendsListActivity.class;
    }
}
