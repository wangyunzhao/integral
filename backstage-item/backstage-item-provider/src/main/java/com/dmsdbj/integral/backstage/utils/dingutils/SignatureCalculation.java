//package com.dmsdbj.integral.backstage.utils.dingutils;
//
//import org.apache.commons.codec.binary.Base64;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
//public class SignatureCalculation {
//    public String CalSignature(String timestamp ,String appSecret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
//
//
//        // 根据timestamp, appSecret计算签名值
//        String stringToSign = timestamp;
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256"));
//        byte[] signatureBytes = mac.doFinal(stringToSign.getBytes("UTF-8"));
//        String signature = new String(Base64.encodeBase64(signatureBytes));
//        // String urlEncodeSignature = urlEncode(signature);
//        return null;
//    }
//
//
//
//    // encoding参数使用utf-8
//    public static String urlEncode(String value, String encoding) {
//        if (value == null) {
//            return "";
//        }
//        try {
//            String encoded = URLEncoder.encode(value, encoding);
//            return encoded.replace("+", "%20").replace("*", "%2A")
//                    .replace("~", "%7E").replace("/", "%2F");
//        } catch (UnsupportedEncodingException e) {
//            throw new IllegalArgumentException("FailedToEncodeUri", e);
//        }
//    }
//}
