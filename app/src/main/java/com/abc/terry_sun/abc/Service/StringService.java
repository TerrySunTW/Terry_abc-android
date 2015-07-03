package com.abc.terry_sun.abc.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Terry on 2015/4/26.
 */
public class StringService {
    public static Date GetJsonDate(String JsonDate)
    {
        int idx1 = JsonDate.indexOf("(");
        int idx2 = JsonDate.indexOf(")");
        String s = JsonDate.substring(idx1 + 1, idx2);
        long l = Long.valueOf(s);
        return new Date(l);
    }
}
