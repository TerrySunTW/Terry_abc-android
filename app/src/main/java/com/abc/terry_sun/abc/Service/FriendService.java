package com.abc.terry_sun.abc.Service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.abc.terry_sun.abc.CustomClass.Application.ABCApplication;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Entities.DB_Friend;
import com.abc.terry_sun.abc.FriendsListActivity;
import com.abc.terry_sun.abc.MainActivity;
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
        List<DB_Friend> list=DB_Friend.listAll(DB_Friend.class);
        list.remove(0);
        return list;
    }
    public DB_Friend GetMyInfo()
    {
        List<DB_Friend> list=DB_Friend.listAll(DB_Friend.class);
        return list.get(0);
    }

    public void AddFriendDialog() {
        final InputMethodManager imm = (InputMethodManager) ABCApplication.getSugarContext().getSystemService(ABCApplication.getSugarContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        final AlertDialog.Builder AddFriendDialog = new AlertDialog.Builder(MainActivity.GetMainActivityContext());
        AddFriendDialog.setTitle("Please input friend ID:");

        final EditText EditTextFriendIDInput = new EditText(MainActivity.GetMainActivityContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        EditTextFriendIDInput.setLayoutParams(lp);
        AddFriendDialog.setView(EditTextFriendIDInput);
        AddFriendDialog.setPositiveButton("確認",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(imm.isActive()) {
                            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);

                            new AsyncTaskHttpRequest(MainActivity.GetMainActivityContext(),
                                    new AsyncTaskProcessingInterface() {
                                        @Override
                                        public void DoProcessing() {
                                            //send to server
                                            ServerCommunicationService.getInstance().AddFriend(EditTextFriendIDInput.getText().toString());
                                            //update friends info
                                            ServerCommunicationService.getInstance().UpdateFriendInfo();
                                        }
                                    },
                                    new AsyncTaskPostProcessingInterface() {
                                        @Override
                                        public void DoProcessing() {
                                            FriendsListActivity.UpdateList();
                                        }
                                    }
                            ).execute();

                        }
                    }
                });

        AddFriendDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        dialog.cancel();
                    }
                });
        AddFriendDialog.show();
    }
}
