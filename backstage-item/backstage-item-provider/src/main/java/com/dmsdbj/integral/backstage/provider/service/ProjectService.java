package com.dmsdbj.integral.backstage.provider.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.ProjectModel;
import com.dmsdbj.integral.backstage.model.ProjectNameIdModel;
import com.dmsdbj.integral.backstage.pojo.PluginEntity;
import com.dmsdbj.integral.backstage.pojo.ProjectApprovalEntity;
import com.dmsdbj.integral.backstage.pojo.ProjectEntity;
import com.dmsdbj.integral.backstage.provider.dao.PluginDao;
import com.dmsdbj.integral.backstage.provider.dao.ProjectDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 项目管理
 * @Author 冯瑞姣
 * @Date: 2020-08-05 17:38
 * @Version: 1.0
 **/

@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;
    @Resource
    private PluginDao pluginDao;
    Map result=new HashMap();
    /**
     * @Description 添加项目
     * @Author 冯瑞姣
     * @Date: 2020-08-06 10:35
     * @Version: 1.0
     **/
    public IntegralResult addProject(ProjectModel projectModel) {
        try {
            //雪花算法
            String Id = IdWorker.getIdStr();
            projectModel.setId(Id);

            //判断项目名称和英文名称是否重复
            IntegralResult integralResult = projectAddIsRepeat(projectModel.getName(), projectModel.getEnglishName());
            if (integralResult.getCode() == "1111") {
                return integralResult;
            }
            //添加项目
            int addProjectFlag = projectDao.addProject(projectModel);
            if (addProjectFlag != 0) {
                //TODO:
                System.out.printf("添加项目成功！");
            } else {

                return IntegralResult.build(IntegralResult.FAIL, "添加项目失败", projectModel);

            }
            //添加审批
            ProjectApprovalEntity projectApprovalEntity = new ProjectApprovalEntity();
            String approveId = IdWorker.getIdStr();
            projectApprovalEntity.setId(approveId);
            projectApprovalEntity.setEnglishName(projectModel.getEnglishName());
            projectApprovalEntity.setName(projectModel.getName());
            projectApprovalEntity.setInitiator(projectModel.getOperator());
            System.out.printf(projectApprovalEntity.getInitiator());
            projectApprovalEntity.setOperator(projectModel.getOperator());
            System.out.printf(projectApprovalEntity.toString());
            int addApprovalFlag = projectDao.addApproval(projectApprovalEntity);
            if (addApprovalFlag != 0) {
                //TODO:
                System.out.printf("添加审批成功！");
            } else {
                return IntegralResult.build(IntegralResult.FAIL, "添加审批失败", projectApprovalEntity);
            }
            //TODO:调用发送邮件接口

            return IntegralResult.build(IntegralResult.SUCCESS, "添加项目和审批成功！");
        } catch (Exception e) {
            return IntegralResult.build(IntegralResult.FAIL, "添加失败");
        }
    }

    /**
     * @Description 添加项目判重
     * @Author 冯瑞姣
     * @Date: 2020-08-08 10:38
     * @Version: 1.0
     **/
    public IntegralResult projectAddIsRepeat(String name, String englishName) {
        try {
            ProjectEntity projectEntity = new ProjectEntity();
            //项目名称
            projectEntity.setName(name);
            //项目英文名称
            projectEntity.setEnglishName(englishName);
            List<ProjectEntity> projectModel = projectDao.projectAddIsRepeat(projectEntity);
            if (projectModel.size() == 0) {
                return IntegralResult.build(IntegralResult.SUCCESS, "项目无重复");
            } else {
                return IntegralResult.build(IntegralResult.FAIL, "项目有重复", projectModel);
            }
        } catch (Exception e) {
            return IntegralResult.build(IntegralResult.FAIL, "查询失败");
        }
    }

    /**
     * @Description 查询项目添加记录
     * @Author 冯瑞姣
     * @Date: 2020-08-06 10:35
     * @Version: 1.0
     **/
    public IntegralResult projectAddRecord(String page, String size) {
        try {

            Integer pageNo=Integer.parseInt(page);
            Integer pageSize=Integer.parseInt(size);

            pageNo = pageNo ==  null ?1:pageNo;
            pageSize=pageSize == null ?5:pageSize;


            PageHelper.startPage(pageNo,pageSize);
            List<ProjectEntity> projectEntity = projectDao.projectAddRecord();
            PageInfo<ProjectEntity> projectInfo=new PageInfo<>(projectEntity);

            //总数据量
            result.put("count",projectInfo.getTotal());
            result.put("data",projectEntity);

            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", result);
        } catch (Exception e) {
            return IntegralResult.build(IntegralResult.FAIL, "查询失败");
        }
    }

    /**
     * @Description 模糊搜索项目记录
     * @Author 冯瑞姣
     * @Date: 2020-08-08 10:38
     * @Version: 1.0
     **/
    public IntegralResult searchProjectRecord(String content, String page, String size) {
        try {
            //每页的行数
            int userSize = Integer.parseInt(size);
            //分页开始的行数
            int userPage = (Integer.parseInt(page) - 1) * userSize;

            Integer pageNo=Integer.parseInt(page);
            Integer pageSize=Integer.parseInt(size);

            pageNo = pageNo ==  null ?1:pageNo;
            pageSize=pageSize == null ?5:pageSize;

            PageHelper.startPage(pageNo,pageSize);
            List<ProjectEntity> projectEntity = projectDao.searchProjectRecord(content);
            PageInfo<ProjectEntity> projectInfo=new PageInfo<>(projectEntity);

            //总数据量
            result.put("count",projectInfo.getTotal());
            result.put("data",projectEntity);

            return IntegralResult.build(IntegralResult.SUCCESS, "查询成功", result);
        } catch (Exception e) {
            return IntegralResult.build(IntegralResult.FAIL, "查询失败");
        }
    }

    /**
     * @Description 删除项目
     * @Author 冯瑞姣
     * @Date: 2020-08-10 16:32
     * @Version: 1.0
     **/
    public IntegralResult deleteProjectRecord(List<ProjectModel> projectModel) {
        try {
            Integer deleteRecord = projectDao.deleteProjectRecord(projectModel);
            if(deleteRecord > 0){
                projectModel.forEach(child -> {
                    List<PluginEntity> pluginEntities = pluginDao.queryProjectId(child.getId());
                    if(!CollectionUtils.isEmpty(pluginEntities)){
                        pluginDao.updateDeletePlugin(child.getId());
                    }
                });
                return IntegralResult.build(IntegralResult.SUCCESS, "删除成功");
            }else {
                return IntegralResult.build(IntegralResult.SUCCESS, "没有要删除的记录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IntegralResult.build(IntegralResult.FAIL, "删除失败");
        }
    }

    /**
     * @Description 编辑项目（普通项目组组长）
     * @Author 冯瑞姣
     * @Date: 2020-08-08 10:38
     * @Version: 1.0
     **/
    public IntegralResult editProject(String id, String name) {
        try {
            //判断项目名称是否重复
            IntegralResult integralResult = projectEditIsRepeat(name,id);
            if (integralResult.getCode() == "1111") {
                return integralResult;
            }
            //根据项目id更改项目名称
            projectDao.updateProjectName(id, name);
            return IntegralResult.build(IntegralResult.SUCCESS, "编辑成功");
        } catch (Exception e) {
            return IntegralResult.build(IntegralResult.FAIL, "编辑失败");
        }
    }

    /**
     * @Description 编辑项目（有权限）
     * @Author 冯瑞姣
     * @Date: 2020-08-08 10:38
     * @Version: 1.0
     **/
    public IntegralResult editProjectPower(String id, String name, String exchangeRate) {
        try {

            //判断项目名称是否重复
            IntegralResult integralResult = projectEditIsRepeat(name,id);
            if (integralResult.getCode() == "1111") {
                return integralResult;
            }
            //根据项目id更改项目名称和兑换比率
            projectDao.updateProjectPower(id, name, exchangeRate);

            return IntegralResult.build(IntegralResult.SUCCESS, "编辑成功");
        } catch (Exception e) {
            return IntegralResult.build(IntegralResult.FAIL, "编辑失败");
        }
    }

    /**
     * @Description 编辑项目判重
     * @Author 冯瑞姣
     * @Date: 2020-08-08 10:38
     * @Version: 1.0
     **/
    public IntegralResult projectEditIsRepeat(String name,String id) {
        try {

//            ProjectEntity projectEntity = new ProjectEntity();
            //获取项目id，根据项目id判断项目名称是否重复S
//            projectEntity.setName(name);
            int count = projectDao.projectEditIsRepeat(name,id);
            System.out.println(count);
            if (count == 0) {
                return IntegralResult.build(IntegralResult.SUCCESS, "项目名称无重复");
            } else {
                return IntegralResult.build(IntegralResult.FAIL, "项目名称有重复");
            }
        } catch (Exception e) {
            return IntegralResult.build(IntegralResult.FAIL, "查询失败");
        }
    }

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [id]
    * @date: 2020/8/13
    * @time: 20:10
    * @description:根据id查询项目名称
    */
    public IntegralResult projectName(String id){
        try {

           return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",projectDao.projectName(id));
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"查询失败");
        }
    }

    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/13
    * @time: 20:10
    * @description:查询所有没有被删除的项目名称和id
    */
    public IntegralResult projectNameId(){
        try {

            //Map result=new HashMap();
            //Integer pageNo=Integer.parseInt(page);
            //Integer pageSize=Integer.parseInt(size);
            //
            //pageNo = pageNo ==  null ?1:pageNo;
            //pageSize=pageSize == null ?5:pageSize;


            //PageHelper.startPage(pageNo,pageSize);
            List<ProjectNameIdModel> approveModel=projectDao.projectNameId();
            //PageInfo<ProjectNameIdModel> projectInfo=new PageInfo<>(approveModel);

            //总数据量
            //result.put("count",projectInfo.getTotal());
            //result.put("data",approveModel);
            return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",approveModel);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"查询失败");
        }
    }
}
