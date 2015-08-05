package com.abc.terry_sun.abc.CustomClass.Application;

import com.abc.terry_sun.abc.R;
import com.orm.SugarApp;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by terry_sun on 2015/7/27.
 */
@ReportsCrashes(
        mailTo = "terry.sun.tw@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class ABCApplication extends SugarApp {
        @Override
        public void onCreate() {
                super.onCreate();
                ACRA.init(this);
        }
}
