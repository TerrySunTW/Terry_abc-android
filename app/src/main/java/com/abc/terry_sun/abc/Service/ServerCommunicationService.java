package com.abc.terry_sun.abc.Service;

import android.util.Log;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Utilits.OkHttpUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by terry_sun on 2015/7/24.
 */
public class ServerCommunicationService {
    String TAG="ServerCommunicationService";
    private static final ServerCommunicationService _ServerCommunicationService = new ServerCommunicationService();
    public static ServerCommunicationService getInstance() {
        return _ServerCommunicationService;
    }
    public void ChangeFavoriteCard(final String EntityCardID)
    {
        AsyncTaskHttpRequest _AsyncTaskHttpRequest = new AsyncTaskHttpRequest(new AsyncTaskProcessingInterface() {
            @Override
            public void DoProcessing() {
                try {
                    List<BasicNameValuePair> UrlParams = new LinkedList<BasicNameValuePair>();
                    UrlParams.add(new BasicNameValuePair("UserFacebookID", VariableProvider.getInstance().getFacebookID()));
                    UrlParams.add(new BasicNameValuePair("EntityCardID", EntityCardID));
                    OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.ToggleFavoriteSetting, UrlParams));
                }
                catch (Exception ex)
                {
                    Log.e(TAG,ex.getMessage());
                }
            }
        });
        _AsyncTaskHttpRequest.execute();
    }
}
