package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.ProjectModel;
import com.dmsdbj.integral.backstage.model.ProjectNameIdModel;
import com.dmsdbj.integral.backstage.model.ProjectNameModel;
import com.dmsdbj.integral.backstage.pojo.ProjectApprovalEntity;
import com.dmsdbj.integral.backstage.pojo.ProjectEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 项目管理
 * @Author 冯瑞姣
 * @Date: 2020-08-06 10:52
 * @Version: 1.0
 **/

@Repository("projectDao")
public interface ProjectDao {
    /**
     * @Description 添加项目
     * @Author 冯瑞姣
     * @Date: 2020-08-06 10:52
     * @Version: 1.0
     **/
    int addProject(ProjectModel projectModel);

    /**
     * @Description 添加审批
     * @Author 冯瑞姣
     * @Date: 2020-08-06 10:52
     * @Version: 1.0
     **/
    int addApproval(@Param("projectApproval") ProjectApprovalEntity projectApprovalEntity);


    /**
     * @Description 添加项目判重
     * @Author 冯瑞姣
     * @Date: 2020-08-08 19:44
     * @Version: 1.0
     **/
    List<ProjectEntity> projectAddIsRepeat(ProjectEntity projectEntity);

    /**
     * @Description 查询项目添加记录
     * @Author 冯瑞姣
     * @Date: 2020-08-08 10:45
     * @Version: 1.0
     **/
    List<ProjectEntity> projectAddRecord();

    /**
     * @Description 模糊搜索项目记录
     * @Author 冯瑞姣
     * @Date: 2020-08-08 11:29
     * @Version: 1.0
     **/
    List<ProjectEntity> searchProjectRecord(String content);

    /**
     * @Description 删除项目
     * @Author 冯瑞姣
     * @Date: 2020-08-10 16:34
     * @Version: 1.0
     **/
    Integer deleteProjectRecord(List<ProjectModel> projectModel);

    /**
     * @Description 编辑项目（普通项目组组长）
     * @Author 冯瑞姣
     * @Date: 2020-08-08 20:50
     * @Version: 1.0
     **/
    void updateProjectName(String id,String name);

    /**
     * @Description 编辑项目（有权限）
     * @Author 冯瑞姣
     * @Date: 2020-08-08 21:05
     * @Version: 1.0
     **/
    void updateProjectPower(String id,String name,String exchangeRate);

    /**
     * @Description 编辑项目判重
     * @Author 冯瑞姣
     * @Date: 2020-08-09 09:45
     * @Version: 1.0
     **/
    int projectEditIsRepeat(String name,String id);

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [id]
    * @date: 2020/8/13
    * @time: 20:03
    * @description:
    */
    ProjectNameModel projectName(String id);
    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/13
    * @time: 20:03
    * @description:
    */
    List<ProjectNameIdModel> projectNameId();
}
