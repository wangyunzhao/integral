package com.dmsdbj.integral.backstage.provider.service;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.AuthenticationModel;
import com.dmsdbj.integral.backstage.pojo.ProjectEntity;
import com.dmsdbj.integral.backstage.provider.dao.AuthenticationDao;
import com.dmsdbj.integral.backstage.utils.cache.MD5Utils;
import com.dmsdbj.integral.backstage.utils.cache.RedisContants;
import com.dmsdbj.integral.backstage.utils.cache.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Author: 曹祥铭
 * @Description:
 * @Date: Create in 8:06 2020/7/27
 */
@Service
public class AuthenticationService {


    @Autowired
    private AuthenticationDao authenticationDao;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${effectiveTime}")
    private int effectiveTime;

    public IntegralResult getSignature(AuthenticationModel authenticationModel){
        //1、判断参数是否为空
        if(authenticationModel.getSecretId()==null||authenticationModel.getSecretKey()==null||authenticationModel.getAppName()==null){
            //参数任意一项为空不合法，直接结束
            return IntegralResult.build(IntegralResult.FAIL,"参数不合法！");
        }
        //2、判断SecretID和SecretKey是否存在有效
        ProjectEntity projectEntity=null;
        try {
            projectEntity= authenticationDao.queryProjectBySecretId(authenticationModel.getSecretId());

        }catch(Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"未授权的请求！");
        }
            //判空
        if(projectEntity==null){
            //说明没有查到相关数据，结束
            return IntegralResult.build(IntegralResult.FAIL,"未授权的请求！");
        }
            //校验信息
        boolean engName = projectEntity.getEnglishName().trim().equals(authenticationModel.getAppName().trim());
        boolean secKey = projectEntity.getSecretKey().trim().equals(authenticationModel.getSecretKey().trim());
        //如果有任何一项不符合,就结束
        if(!engName||!secKey){
            //信息匹配失败，结束
            return IntegralResult.build(IntegralResult.FAIL,"没有权限！");
        }
        //判断redis中是否已经存在该项目的signature，有就直接返回，没有就生成一个并存到redis中
        boolean hasSignature = redisUtil.hasKey(RedisContants.signature+authenticationModel.getSecretId());
        String signature="";
        if(!hasSignature){
            //3、调用生成Signature的方法生成签名并返回
            signature= makeSignature(authenticationModel);
        }else{
            signature=redisUtil.get(RedisContants.signature+authenticationModel.getSecretId());
            if("".equals(signature)||signature==null){
                 signature= makeSignature(authenticationModel);
            }
        }
        return IntegralResult.build(IntegralResult.SUCCESS,signature);
    }

    private String makeSignature(AuthenticationModel authenticationModel){
        //1、将参数放入list
        //2、根据SecretID随即生成8位字符，并放入list（防止签名重复）
        //3、将当前时间戳放入list（防止签名重复）
        //4、将list排序（打乱参数的顺序）（防止签名重复）
        //5、将排序后的list中元素使用MD5加密，生成最终Signature
        StringBuffer sb=new StringBuffer();
        List<String > list=new ArrayList<>();
        list.add(authenticationModel.getSecretId().trim());
        list.add(authenticationModel.getSecretKey().trim());
        list.add(authenticationModel.getAppName());
        list.add(LocalDateTime.now().toString());
        list.add(randomStr(authenticationModel.getSecretId()));
        Collections.sort(list);
        list.stream().peek(e->sb.append(e)).collect(Collectors.toList());
        String Signature = MD5Utils.encode(sb.toString());
        //将Signature以及SecretId存入Redis并设置过期时间
        if(effectiveTime==0){
            effectiveTime=7200;
        }
        boolean i = redisUtil.set(RedisContants.signature+authenticationModel.getSecretId(),Signature, Long.valueOf(effectiveTime));
        boolean l = redisUtil.set(RedisContants.signature+Signature,authenticationModel.getSecretId(), Long.valueOf(effectiveTime));
        if(!i||!l){
            //存入redis失败,返回错误
            return "SERVICE_ERROR";
        }
      return Signature ;
    }

    public String randomStr(String str){
        String temp="";
        Random random =new Random();
        for (int i = 0; i < 8; i++) {
            int num =random.nextInt(str.length());
            char c1 =str.charAt(num);
            temp+=c1;
        }
        return temp;

    }

}
