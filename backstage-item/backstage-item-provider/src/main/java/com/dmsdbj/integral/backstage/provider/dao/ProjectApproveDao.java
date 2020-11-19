package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.ProjectApproveModel;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/8/4
 * Time: 19:52
 * Description:${DESCRIPTION}
 */
@Repository
public interface ProjectApproveDao {
    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/5
    * @time: 11:10
    * @description:查询所有待审批状态
    */
    public List<ProjectApproveModel> queryProjectApprove();
    /*
    * @author: 郝龙飞
    * @version:
    * @param: [queryLikeInfo]
    * @date: 2020/8/5
    * @time: 11:13
    * @description:模糊查询待审批状态
    */
    public List<ProjectApproveModel>  queryProjectApproveLike(String queryLikeInfo);

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [id]
    * @date: 2020/8/5
    * @time: 11:14
    * @description:更新审批状态
    */

    public String queryEnglishName(String id);

    public void updateApproveStatus(String id);
    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/5
    * @time: 11:09
    * @description:修改兑换比例
    */
    public void updateProjectRate(String englishName, String exchangeRate,String secretId,String secretKey);
    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/5
    * @time: 11:08
    * @description:通过审批
    */
    public void updateApproveRemark(String id,String remark);

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [englishName]
    * @date: 2020/8/15
    * @time: 20:04
    * @description:更改项目表中的审批状态
    */
    void deleteProjectStatus(String englishName);
}
