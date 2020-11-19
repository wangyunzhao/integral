package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dmsdbj.integral.dingtalk.model.AddIntegralDataModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 *加分dao层
 * @author 王云召
 * @since 2020年6月24日09:35:42
 */
@Repository("addIntegralDao")
public interface AddIntegralDao {
    /**
     * 计算考勤打卡加分分值
     * @param date
     * @return
     */
    int caluIntegral(LocalDate date);

    /**
     * 查询加分数据
     * @param date
     * @return
     */
    List<AddIntegralDataModel> queryAddIntegralData(LocalDate date);

    /**
     * 计算纪委处理异常打卡的加分分数
     * @param addIntegral
     * @param minusIntegral
     * @return
     */
    int caluHandleInfoIntegral(int addIntegral,int minusIntegral);

    /**
     * 查询纪委处理异常打卡的加分数据
     * @return
     */
    List<AddIntegralDataModel> queryHandleAlarmIntegralData();

    /**
     * 加分成功后更新数据的是否加分成功的状态
     * @param addIntegralDatas
     * @return
     */
    int updateIsSuccess(@Param("addIntegralDatas")List<AddIntegralDataModel> addIntegralDatas);

    /**
     * 更新处理状态
     * @param handleAlarmIntegralDatas
     * @return
     */
    int updateIsHandSuccess(@Param("handleAlarmIntegralDatas")List<AddIntegralDataModel> handleAlarmIntegralDatas);

    /**
     * 查询加分成功的消息
     * @param date
     * @return
     */
    List<AddIntegralDataModel> queryAddIntegralAfterSuccess(LocalDate date);
}
