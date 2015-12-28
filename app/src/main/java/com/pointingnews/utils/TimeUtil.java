package com.pointingnews.utils;

import android.os.SystemClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015-12-11.
 */
public class TimeUtil {
    private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

    /**
     * 获取目标时间距离当前的时和分
     * @param str
     * @return
     */
    public static String parseTime(String str){
        try {
            Date date = format1.parse(str);

            long temp = SystemClock.currentThreadTimeMillis();
            long time = temp - date.getTime();

            String dateStr = format2.format(new Date(time));
            return dateStr;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
