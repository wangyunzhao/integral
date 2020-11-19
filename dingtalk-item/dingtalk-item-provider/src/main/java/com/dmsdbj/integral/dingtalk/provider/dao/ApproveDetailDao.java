package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dingtalk.item.pojo.AlarmDetailEntity;
import com.dingtalk.item.pojo.ApproveDetailEntity;
import com.dingtalk.item.pojo.PunchResultEntity;
import com.dmsdbj.integral.dingtalk.model.MapModel;
import io.swagger.models.auth.In;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
/**
* @Description
* @Author  xingming
* @Date   2020/7/22 11:46
* @Param
* @Return
*
*/
@Repository("approveDetailDao")
public interface ApproveDetailDao {
    /**
     * 插入审批详情表
     *
     * @param approveDetailEntity 审批实体
     * @return 受影响的行数
     */
    int insertApproval(@Param("approveDetailEntity") List<ApproveDetailEntity> approveDetailEntity);

    /**
     * 如果process_instance_id存在，删除
     *
     * @param processId 审批id
     * @return 受影响的行数
     */
    List<String> queryProcessInstanceId(String processId);

    /**
     * 如果process_instance_id存在，删除
     *
     * @param processId 审批id
     * @return 受影响的行数
     */
    Integer deleteProcessInstanceId(String processId);

    /**
     * 查询当天已同意，且请假结束时间小于当前时间（job执行的时间）审批
     *
     * @author fjx
     * @param begin_time 获取配置文件中的时间（上午默认是当天 8点  ，下午默认是 当天 13点）
     * @return
     */

    List<ApproveDetailEntity> selectAllApproveDetailAgree(String begin_time);


    /**
     * 查询今日的请假人
     * @return
     * @author 王云召
     */
    List<String> queryLeavePeople();



}
