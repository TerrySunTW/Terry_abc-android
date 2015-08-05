package com.abc.terry_sun.abc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskHttpRequest;
import com.abc.terry_sun.abc.CustomClass.AsyncTask.AsyncTaskProcessingInterface;
import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.Provider.HttpURL_Provider;
import com.abc.terry_sun.abc.Provider.VariableProvider;
import com.abc.terry_sun.abc.Service.ImageService;
import com.abc.terry_sun.abc.Service.ServerCommunicationService;
import com.abc.terry_sun.abc.Service.StorageService;
import com.abc.terry_sun.abc.Service.StringService;
import com.abc.terry_sun.abc.Utilits.InternetUtil;
import com.abc.terry_sun.abc.Utilits.OkHttpUtil;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Terry on 2015/4/19.
 */
public class LoginActivity extends Activity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    Context _context;
    List<BasicNameValuePair> LoginParams= new LinkedList<BasicNameValuePair>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context=this;
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
        updateWithToken(AccessToken.getCurrentAccessToken());

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setLoginBehavior(LoginBehavior.SSO_WITH_FALLBACK);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callback registration
                FacebookLogin();
            }
        });

    }
    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            accessToken=currentAccessToken;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetFacebookInfoProcess();
                }
            }, 10);
        }
    }

    private void FacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        accessToken = loginResult.getAccessToken();
                        GetFacebookInfoProcess();
                        Toast.makeText(getApplication(), "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(getApplication(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(getApplication(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void GetFacebookInfoProcess() {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        GetFacebookUserData(object);
                        AsyncTaskHttpRequest _AsyncTaskHttpRequest=new AsyncTaskHttpRequest(_context,new AsyncTaskProcessingInterface() {
                            @Override
                            public void DoProcessing() {
                                try {
                                    //login
                                    OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(HttpURL_Provider.FacebookLogin, LoginParams));
                                    //get user card info
                                    ServerCommunicationService.getInstance().GetUserCardInfo();
                                } catch (Exception e) {
                                    if (e!=null) {
                                        Log.e("Error", e.getMessage());
                                    }
                                }
                                Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                            }
                        });
                        _AsyncTaskHttpRequest.execute();

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
    String URL;
    private void GetFacebookUserData(JSONObject object) {
        String FacebookID = object.optString("id");
        String FacebookName = object.optString("name");
        String FacebookLink = object.optString("link");
        String FacebookPhoto="";
        Log.d("FB", "complete");
        Log.d("FB",FacebookID);
        Log.d("FB", FacebookName);
        Log.d("FB", FacebookLink);
        try {
            JSONObject jsonPicture = object.getJSONObject("picture");
            JSONObject jsonData = jsonPicture.getJSONObject("data");
            FacebookPhoto= jsonData.optString("url");
            Log.d("FB", FacebookPhoto);
        }
        catch(Exception e)
        {

        }
        LoginParams.add(new BasicNameValuePair("FacebookLoginID", FacebookID));
        LoginParams.add(new BasicNameValuePair("FacebookImage", FacebookPhoto));
        LoginParams.add(new BasicNameValuePair("UserName", FacebookName));
        LoginParams.add(new BasicNameValuePair("FacebookLink", FacebookLink));





        VariableProvider.getInstance().setFacebookID(FacebookID);
        VariableProvider.getInstance().setFacebookUserName(FacebookName);
        VariableProvider.getInstance().setFacebookLink(FacebookLink);
        VariableProvider.getInstance().setFacebookPhotoURL(FacebookPhoto);
    }

    private void GetKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.abc.terry_sun.abc",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", "PackageManager.NameNotFoundException");

        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", "NoSuchAlgorithmException");
        }
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
}
