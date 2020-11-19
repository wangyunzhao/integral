package com.dmsdbj.integral.dingtalk.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dingtalk.item.pojo.DisciplineUserEntity;
import com.dingtalk.item.pojo.TypeUserEntity;
import com.dmsdbj.integral.dingtalk.model.DisciplineUserModel;
import com.dmsdbj.integral.dingtalk.provider.dao.DisciplineUserDao;
import com.dmsdbj.integral.dingtalk.provider.dao.TypeUserDao;
import com.dmsdbj.integral.dingtalk.utils.http.HttpUtils;
import com.dmsdbj.integral.dingtalk.utils.http.ResponseWrap;
import com.dmsdbj.integral.dingtalk.utils.http.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 查询当月当值纪委
 *
 * @author 王梦瑶
 * @since 2020年6月21日21:07:28
 */
@Service
@Slf4j
@RefreshScope
public class DisciplineUserService {
    @Value("${AppKey}")
    private String appkey;
    @Value("${AppSecret}")
    private String appsecret;
    @Resource
    private DisciplineUserDao disciplineUserDao;

    @Resource
    private TypeUserDao typeUserDao;
    /**
     * 查询当月当值纪委
     *
     * @author 王梦瑶
     * @since 2020年6月21日21:07:28
     */
    public List<DisciplineUserEntity> queryDisciplineUser() {
        //查询组织机构下的所有期的所有纪委

        List<DisciplineUserEntity>list=disciplineUserDao.queryDisciplineUser();
        if (list!=null && list.size()>0){
            disciplineUserDao.deleteDisciplineUser();
        }
        //调用钉钉获取部门列表接口
        String url = "https://oapi.dingtalk.com/department/list?access_token=" + TokenUtils.getAccessToken(appkey, appsecret);

        HttpUtils get = HttpUtils.get(url);
        get.addHeader("Content-Type", "application/json; charset=utf-8");
        ResponseWrap execute = get.execute();
        String resultStr = execute.getString();
        Object parse = JSONObject.parse(resultStr);
        JSONObject object = (JSONObject) parse;
        String disciplineUserStr = object.getString("department");
        List<DisciplineUserModel> disciplineUserModelList = JSONObject.parseArray(disciplineUserStr, DisciplineUserModel.class);
        List<DisciplineUserModel> usermodel = disciplineUserModelList.stream().filter(item ->!"1".equals(item.getId())).collect(Collectors.toList());

        //过滤出name=纪律委员会得实体
        List<DisciplineUserModel> disciplineUserModelList1 = disciplineUserModelList.stream().filter
                (item -> "纪律委员会".equals(item.getName())).collect(Collectors.toList());

        //获得纪律委员会的id
        String id = disciplineUserModelList1.get(0).getId();
        //根据纪律委员会的id作为父id筛选各个期的部门id（id）
        List<DisciplineUserModel> collectid = usermodel.stream().filter(item -> item.getParentid().equals(id) && !"总负责人".equals(item.getName())).collect(Collectors.toList());
        List<DisciplineUserEntity> disciplineUserEnties = new ArrayList<>();

        for (DisciplineUserModel collect : collectid
        ) {

            DisciplineUserEntity disciplineUserEntity = new DisciplineUserEntity();
            //获得部门id
            String departmentId = collect.getId();
            //获得用户期数
            String level = collect.getName();
            //根据获取的部门id调用钉钉接口--获取部门用户
            String urlUser = "https://oapi.dingtalk.com/user/simplelist?access_token=" + TokenUtils.getAccessToken(appkey, appsecret) + "&department_id=" + departmentId;
            HttpUtils get1 = HttpUtils.get(urlUser);
            get1.addHeader("Content-Type", "application/json; charset=utf-8");
            ResponseWrap execute1 = get1.execute();
            String userlist = execute1.getString();
            Object parse1 = JSONObject.parse(userlist);
            JSONObject object1 = (JSONObject) parse1;
            String disciplineUser = object1.getString("userlist");
            if (disciplineUser.equals(null)||"[]".equals(disciplineUser)){
                System.out.println(level+"当月没有当值纪委");
                continue;
            }
            List<DisciplineUserModel> disciplineUserList = JSONObject.parseArray(disciplineUser, DisciplineUserModel.class);
            //得到纪委姓名
            String userName = disciplineUserList.get(0).getName();
            //得到纪委dingid
            String dingId = disciplineUserList.get(0).getUserId();
            disciplineUserEntity.setLevelNum(level);
            disciplineUserEntity.setUserDingId(dingId);
            disciplineUserEntity.setUserName(userName);
            disciplineUserEntity.setId(IdWorker.getIdStr());
            //根据dingId查询出组织id
            String organationId = disciplineUserDao.queryOrganation(dingId);
            //把组织id放入纪委实体
            disciplineUserEntity.setOrganizationId(organationId);
            disciplineUserEnties.add(disciplineUserEntity);

        }
        //插入数据库
        disciplineUserDao.insertDiscipline(disciplineUserEnties);
        //查询type_user表中是否已经存在此用户，如果存在则不操作，如果不存在则插入，获取权限1——纪委
        List<String> typeUserEntities = typeUserDao.selectAllTypeUserByType(1);
        if(typeUserEntities!=null&& CollectionUtils.isNotEmpty(typeUserEntities)){
            List<TypeUserEntity> collect = disciplineUserEnties.stream().filter(e -> !typeUserEntities.contains(e.getUserDingId())).map(b -> {
                TypeUserEntity typeUserEntity = new TypeUserEntity();
                typeUserEntity.setUserDingId(b.getUserDingId());
                typeUserEntity.setType("1");
                return typeUserEntity;
            }).collect(Collectors.toList());
            //将新的纪委信息存入type_user表中
            //判空
            if(collect!=null&&CollectionUtils.isNotEmpty(collect)){
                typeUserDao.insertTypeUser(collect);
            }
        }

        //获得用户姓名 和钉钉id
        //根据用户姓名查询出期数和组织id
        //存库
        return disciplineUserEnties;

    }
}
