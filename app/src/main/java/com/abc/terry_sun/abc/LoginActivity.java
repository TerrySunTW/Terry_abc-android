package com.abc.terry_sun.abc;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskPostProcessingInterface;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Service.AppUpdateService;
import com.abc.terry_sun.abc.Service.ProcessControlService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.abc.terry_sun.abc.Utilits.InternetUtil;
import com.abc.terry_sun.abc.Utilits.OkHttpUtil;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by Terry on 2015/4/19.
 */
public class LoginActivity extends AppCompatActivity {
    String TAG="LoginActivity";
    LoginButton loginButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    static Context _context;
    static Boolean IsVersionSameWithServer=false;
    ContentValues LoginParams= new ContentValues();
    TextView TextViewMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context=this;
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        //Log.e(TAG, "Key:" + FacebookService.GetKeyHash(this));

        TextViewMessage = (TextView) findViewById(R.id.Message);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        if(!InternetUtil.IsOnline(this))
        {
            TextViewMessage.setText("Can't Connect to Server, Please Try Again Later.");
        }
        else
        {
            FacebookLoginSetting();
        }
    }

    private void FacebookLoginSetting() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //Log.e(TAG, "onSuccess");
                        accessToken = loginResult.getAccessToken();
                        GetFacebookInfoProcess();
                        Toast.makeText(getApplication(), "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        //Log.e(TAG, "onCancel");
                        Toast.makeText(getApplication(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        //Log.e(TAG, "onError");
                        Toast.makeText(getApplication(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
        //login
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    private void GetFacebookInfoProcess() {
        //Log.e(TAG, "GetFacebookInfoProcess");

        GraphRequestBatch batch = new GraphRequestBatch(

                GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                GetFacebookUserData(object);
                                AsyncTaskHttpRequest _AsyncTaskHttpRequest=new AsyncTaskHttpRequest(_context, new AsyncTaskProcessingInterface() {
                                    @Override
                                    public void DoProcessing() {
                                        try {
                                            IsVersionSameWithServer = ServerCommunicationService.getInstance().IsVersionSameWithServer(_context);
                                            if (IsVersionSameWithServer) {
                                                //login
                                                OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.FacebookLogin, LoginParams));
                                                //get user card info
                                                ServerCommunicationService.getInstance().UpdateServerInfo();
                                                Intent intent = new Intent();
                                                intent.setClass(LoginActivity.this, MainActivity.class);
                                                ProcessControlService.CloseProgressDialog();
                                                startActivity(intent);
                                                finish();
                                            }
                                        } catch (Exception e) {
                                            if (e != null) {
                                                Log.e("Error", e.getMessage());
                                            }
                                        }
                                    }
                                }, new AsyncTaskPostProcessingInterface() {
                                    @Override
                                    public void DoProcessing() {
                                        if(IsVersionSameWithServer)
                                        {
                                            AppUpdateService.getInstance().RemoveAPK();
                                        }
                                        else
                                        {
                                            AppUpdateService.getInstance().DownloadAPK(_context);
                                        }
                                    }
                                });
                                _AsyncTaskHttpRequest.execute();

                            }
                        })
        );
        batch.executeAsync();
    }
    private void GetFacebookUserData(JSONObject object) {
        //Log.e(TAG, "GetFacebookUserData");
        String FacebookID = object.optString("id");
        String FacebookName = object.optString("name");
        String FacebookLink = object.optString("link");
        String FacebookPhoto="";
        //Log.d("FB", "complete");
        //Log.d("FB",FacebookID);
        //Log.d("FB", FacebookName);
        FacebookPhoto= "https://graph.facebook.com/"+FacebookID+"/picture?type=large";
        LoginParams.put("FacebookLoginID", FacebookID);
        LoginParams.put("FacebookLink", FacebookLink);
        LoginParams.put("FacebookImage", FacebookPhoto);
        LoginParams.put("UserName", FacebookName);


        VariableProvider.getInstance().setFacebookID(FacebookID);
        VariableProvider.getInstance().setFacebookUserName(FacebookName);
        VariableProvider.getInstance().setFacebookLink(FacebookLink);
        VariableProvider.getInstance().setFacebookPhotoURL(FacebookPhoto);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e(TAG, "onResume");
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Log.e(TAG, "onPause");

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e(TAG, "onActivityResult");
        callbackManager.onActivityResult(requestCode,resultCode, data);
    }

}
