package com.dmsdbj.integral.dingtalk.utils.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @program: integral
 * @Date: 2020-06-13 11:11
 * @Author: Jason
 * @Description:
 */

@Component
public class SecondAlarmUtil {

    /**
     * 获取配置文件数据 —— 星期三
     */
    @Value("${secondAlarm.wednesday}")
    private String wednesday;

    /**
     * 获取配置文件数据 —— 星期五
     */
    @Value("${secondAlarm.sunday}")
    private String sunday;

    /**
     * 获取配置文件数据 —— 时间
     */
    @Value("${secondAlarm.time}")
    private String[] time;

    /**
     * 判断当天是星期几
     */
    public String week() {
        return new SimpleDateFormat("EEEE").format(new Date());
    }

    /**
     * 获取当前日期 ,设置当前日期的格式为年-月-日
     */
    public String getCurrentDate() {
        return new SimpleDateFormat("YYYY-MM-dd").format(new Date());
    }

    /**
     * 判断当前是上午还是下午（上午是 0 , 下午是 1）
     */
    public int amOrPm() {
        return new GregorianCalendar().get(GregorianCalendar.AM_PM);
    }

    /**
     * 设置开始时间,如果是上午，开始时间则为 当前日期+8:00 ，如果是下午，开始时间为当前日期+14:00
     */
    public String beginTime() {
        String beginTime = null;
        if (amOrPm() == 0) {
            beginTime = getCurrentDate() + " " + time[0];
        } else {
            beginTime = getCurrentDate() + " " + time[2];
        }
        return beginTime;
    }

    /**
     * 如果当前月份在5月到10月之间，下午两小时请假，应该打开时间为16:30
     */
    public String checkMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        if (month >= 5 && month <= 10) {
            return getCurrentDate() + " " + "16:30:00";
        }
        return getCurrentDate() + " " + "16:00:00";

    }
    /**
     * 将string类型转换为localtime类型
     */
    public LocalDateTime toChange(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeStr = checkMonth();
        LocalDateTime baseChecktime = LocalDateTime.parse(timeStr, formatter);
        return baseChecktime;
    }

    /**
     * 设置应该打卡时间，如果是上午，默认是10:00 如果是下午，要区分是不是5月到10月时间
     */
    public String baseCheckTime() {
        String baseCheckTime = null;
        if (amOrPm() == 0) {
            baseCheckTime = getCurrentDate() + " " + "10:00:00";
        } else {
            baseCheckTime=checkMonth();
        }
        return baseCheckTime;
    }

    /**
     * 判断是不是周几下午，如果是，则返回false 否则返回true
     */
    public boolean weekNoon() {
        String week=week();
        if (sunday.equals(week) || wednesday.equals(week)) {
            if (amOrPm() == 1) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
