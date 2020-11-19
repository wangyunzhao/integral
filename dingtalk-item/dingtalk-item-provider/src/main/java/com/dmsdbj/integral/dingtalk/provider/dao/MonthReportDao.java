package com.dmsdbj.integral.dingtalk.provider.dao;

import com.dmsdbj.integral.dingtalk.model.MonthReportModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导出月报
 *
 * @author 崔晓鸿
 * @since 2020年6月11日08:57:48
 */
@Repository("monthReportDao")
public interface MonthReportDao {
    /**
     * 查询违纪与请假
     *
     * @param organizationId 组织id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<MonthReportModel>
     * @author 崔晓鸿
     * @since 2020年6月8日09:45:15
     */
    List<MonthReportModel> selectMonthReport(String organizationId, String startTime, String endTime);

}
