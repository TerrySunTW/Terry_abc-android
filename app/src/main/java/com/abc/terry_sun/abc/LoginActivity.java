package com.abc.terry_sun.abc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


/**
 * Created by Terry on 2015/4/19.
 */
public class LoginActivity extends Activity {
    CallbackManager callbackManager;
    private AccessToken accessToken;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //初始化FacebookSdk，記得要放第一行，不然setContentView會出錯

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //宣告callback Manager

        callbackManager = CallbackManager.Factory.create();

        //找到login button

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        //幫loginButton增加callback function

        //這邊為了方便 直接寫成inner class

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //登入成功

            @Override
            public void onSuccess(LoginResult loginResult) {

                //accessToken之後或許還會用到 先存起來

                accessToken = loginResult.getAccessToken();

                Log.d("FB", "access token got.");

                //send request and call graph api

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            //當RESPONSE回來的時候

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

                                Log.d("FB","complete");
                                Log.d("FB",object.optString("name"));
                                Log.d("FB",object.optString("link"));
                                Log.d("FB",object.optString("id"));

                            }
                        });

                //包入你想要得到的資料 送出request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            //登入取消

            @Override
            public void onCancel() {
                // App code

                Log.d("FB","CANCEL");
            }

            //登入失敗

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.d("FB",exception.toString());
            }
        });
    }
}
