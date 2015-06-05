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
public class TabGroup_Cards  extends ActivityGroup {
    public static ActivityGroup group;
    public static ArrayList<View> ActivityView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = this;
        ActivityView = new ArrayList<View>();
    }

    @Override
    public void onBackPressed(){
        int length = ActivityView.size();
        //Log.e("Event", "length1:"+String.valueOf(length));
        if ( length > 1) {
            View _view = ActivityView.get(length-2);
            RemoveActive(length-1);//刪掉最後一次看到的畫面
            RemoveActive(length-2);//刪掉原本的自已
            ChangeActivity(_view, true);
        }
        else
        {
            //escpe app
            ProcessControlService.QuitProcess(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int length = ActivityView.size();
        if(length>1)
        {
            onBackPressed();
        }
        else
        {
            //第一次點，預設第一頁
            Intent intent = new Intent(this, CardsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ChangeActivity("tab_purchase_function_list", intent, true);
        }
    }
    public static void ChangeActivity(String IntentName,Intent _intent,Boolean WantSaved)
    {
        Window w = group.getLocalActivityManager().startActivity(IntentName+_intent.hashCode(),_intent);
        View view = w.getDecorView();
        System.gc();
        ChangeActivity(view, WantSaved);
    }

    public static void ChangeActivity(View _view,Boolean WantSaved)
    {
        if(WantSaved)
        {
            ActivityView.add(_view);
        }
        group.setContentView(_view);
        //Log.e("Event", "length4:"+String.valueOf(ActivityView.size()));
    }
    public static void GoPreviousView()
    {
        int length = ActivityView.size();
        if ( length > 0) {
            View _view = ActivityView.get(length - 2);
            RemoveActive(length - 1);//刪掉最後一次看到的畫面
            RemoveActive(length - 2);//刪掉原本的自已
            ChangeActivity(_view, true);
        }
    }

    private static void RemoveActive(int index)
    {
        //Log.e("Event", "length3:"+String.valueOf(ActivityView.size()));
        if(ActivityView.size()>index && index>=0)
        {
            ActivityView.remove(index);
        }
    }
    public static void CleanActiveAndGoFirst()
    {
        int length = ActivityView.size();
        View _view = ActivityView.get(0);
        ActivityView = new ArrayList<View>();
        ChangeActivity(_view,true);
    }

    public static void HideTab()
    {
        TabActivity _TabActivity = (TabActivity)group.getParent();
        _TabActivity.getTabHost().getTabWidget().setVisibility(View.GONE);
    }

}
