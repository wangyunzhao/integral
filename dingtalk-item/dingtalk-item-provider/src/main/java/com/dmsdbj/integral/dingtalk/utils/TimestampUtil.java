package com.dmsdbj.integral.dingtalk.utils;

import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author 刘子腾
 * @DESCRIPTION 日期时间戳转换工具类
 * @create 2019/3/7
*/
public class TimestampUtil {

    /**
     * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：yyyy-MM-dd HH:mm:ss
     */
    public static String convertTimeToString(Long time){
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ftf.format( LocalDateTime.ofInstant( Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    /**
     * 将Long类型的时间戳转换成String 类型的时间格式，时间格式为：MM-dd HH:mm
     */
    public static String convertTimeToShortString(Long time){
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        return ftf.format( LocalDateTime.ofInstant( Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    /**
     * 将字符串转日期成Long类型的时间戳，格式为：yyyy-MM-dd HH:mm:ss
     */
    public static Long convertTimeToLong(String time) {
        Assert.notNull(time, "time is null");
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(time, ftf);
        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当天00:00:00时间戳
     * @return
     */
    public static Long getFirstTime(){
        String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String datetime=date+" 00:00:00";

        return convertTimeToLong(datetime);
    }

    /**
     * 获取当天12:00:00时间戳
     * @return
     */
    public static Long getSecondtime(){
        String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String datetime=date+" 12:00:00";

        return convertTimeToLong(datetime);
    }

    /**
     * 获取当天18:00:00时间戳
     * @return
     */
    public static Long getThirdtime(){
        String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String datetime=date+" 18:00:00";

        return convertTimeToLong(datetime);
    }

    //获得当前时间
    public static String getTodayDate() {
        //获得当前时间
        DateTimeFormatter fmDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime t = LocalDateTime.now();
        return t.format(fmDate);
    }

}
