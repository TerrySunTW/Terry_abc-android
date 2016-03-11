package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

/**
 * Created by terry_sun on 2015/7/9.
 */
public class BaseFragment extends Fragment {
    MainActivity mActivity;
    private  Stack<Fragment> mStacks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) this.getActivity();
        mStacks=new Stack<Fragment>();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            mStacks.add(fragment);
        }
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean GobackFragment() {
        if(mStacks!=null && mStacks.size()>1)
        {
            mStacks.pop();//drop itself
            replaceFragment(mStacks.pop(),true);
            return true;
        }
        return false;
    }
    public void GoFirstFragment() {
        if(mStacks!=null && mStacks.size()>1)
        {
            Fragment first=mStacks.firstElement();
            mStacks.clear();
            replaceFragment(first, true);
        }
    }

}
