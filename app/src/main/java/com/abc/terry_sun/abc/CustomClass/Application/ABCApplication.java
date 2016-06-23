package com.abc.terry_sun.abc.CustomClass.Application;

import android.content.Context;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.Models.ListItem_Actions;
import com.abc.terry_sun.abc.R;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.orm.SugarApp;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by terry_sun on 2015/7/27.
 */
@ReportsCrashes(
        mailTo = "terry.sun.tw@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class ABCApplication extends SugarApp {
        private static Context context;
        @Override
        public void onCreate() {
                super.onCreate();
                TypefaceProvider.registerDefaultIconSets();
                ABCApplication.context = getApplicationContext();
                //ACRA.init(this);
        }
        public static Context getAppContext() {
                return ABCApplication.context;
        }
}
