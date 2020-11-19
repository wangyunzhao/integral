package com.dmsdbj.integral.backstage.utils.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 8:57 2020/7/27
 */
public class MD5Utils {
    public static String encode(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte[] byteDigest = md.digest();

            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];

                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            } // for

            // 32位加密
            return buf.toString();
            // 16位加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
