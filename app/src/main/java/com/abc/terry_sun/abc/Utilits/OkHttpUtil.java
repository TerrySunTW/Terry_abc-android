package com.abc.terry_sun.abc.Utilits;

/**
 * Created by terry_sun on 2015/6/25.
 */
import android.content.ContentValues;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class OkHttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    static{
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }
    /**
     * 该不会开启异步线程。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }
    public static int getIntFromServer(String url) throws IOException{
        int Result=0;
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();

            try {
                Result=Integer.parseInt(responseUrl);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        } else {
            throw new IOException("Unexpected code " + response);
        }
        return Result;
    }
    public static String getStringFromServer(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    public static boolean getBooleanFromServer(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            Gson gson = new Gson();
            Boolean Result=gson.fromJson(responseUrl, boolean.class);
            return Result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    private static final String CHARSET_NAME = "UTF-8";
    /**
     * 这里使用了HttpClinet的API。只是为了方便
     * @param params
     * @return
     */
    public static String formatParams(ContentValues params){
        String paramsURL="";
        Set<Map.Entry<String, Object>> s=params.valueSet();
        Iterator itr = s.iterator();
        while(itr.hasNext())
        {
            if(paramsURL.length()>0)
            {
                paramsURL+="&";
            }
            Map.Entry me = (Map.Entry)itr.next();
            String key = me.getKey().toString();
            String value =  me.getValue().toString();
            paramsURL+=key + "=" + URLEncoder.encode(value);
        }
        //return URLEncodedUtilsHC4.format(params, CHARSET_NAME);
        return paramsURL;
    }
    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(String url, ContentValues params){
        return url + "?" + formatParams(params);
    }
    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }
}