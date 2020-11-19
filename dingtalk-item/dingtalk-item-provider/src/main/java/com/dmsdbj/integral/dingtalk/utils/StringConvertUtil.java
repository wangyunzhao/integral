package com.dmsdbj.integral.dingtalk.utils;

/**
 * @author 刘子腾
 * @DESCRIPTION 下划线驼峰转换工具类
 * @create 2019/3/13
*/
public class StringConvertUtil {

    /**
     * 下划线转换驼峰
     * @param undersoreName
     * @return
     */
    public static String camelCaseName(String undersoreName){
        StringBuilder result=new StringBuilder();
        if (undersoreName!=null && undersoreName.length()>0){
            boolean flag=false;
            for (int i=0;i<undersoreName.length();i++){
                char ch=undersoreName.charAt(i);
                if ("_".charAt(0)==ch){
                    flag=true;
                }else {
                    if (flag){
                        result.append(Character.toUpperCase(ch));
                        flag=false;
                    }else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转下划线
     * @param canmelCaseName
     * @return
     */
    public static String underscoreName(String canmelCaseName){
        StringBuilder result=new StringBuilder();
        if (canmelCaseName!=null && canmelCaseName.length()>0){
            result.append(canmelCaseName.substring(0,1).toLowerCase());
            for (int i=1;i<canmelCaseName.length();i++){
                char ch=canmelCaseName.charAt(i);
                if (Character.isUpperCase(ch)){
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                }else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }
}
