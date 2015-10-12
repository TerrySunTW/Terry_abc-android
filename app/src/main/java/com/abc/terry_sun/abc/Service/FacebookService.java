package com.abc.terry_sun.abc.Service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by terry_sun on 2015/10/7.
 */
public class FacebookService {
    public static String GetKeyHash(Context context) {
        String KeyHash="";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.abc.terry_sun.abc",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", "PackageManager.NameNotFoundException");

        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", "NoSuchAlgorithmException");
        }
        return KeyHash;
    }
}
