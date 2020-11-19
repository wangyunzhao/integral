package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dmsdbj.integral.dingtalk.model.MonthReportModel;
import com.dmsdbj.integral.dingtalk.provider.service.MonthReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 导出月报
 *
 * @author 崔晓鸿
 * @since 2020年6月11日08:56:52
 */
@Api(tags = {"导出月报"})
@RestController
@RequestMapping("monthReport")
public class MonthReportController {
    @Autowired
    private MonthReportService monthReportService;

    /**
     * 根据组织id(相对应的期数)与时间导出月报
     *
     * @author 崔晓鸿
     * @since 2020年6月8日09:41:15
     */
    @ApiOperation(value = "根据组织id(相对应的期数)与时间导出月报")
    @GetMapping("downloadMonthReport")
    public void monthReportDownload(
            HttpServletResponse response,
            @ApiParam(value = "组织id",required = false) @RequestParam(required = false) String organizationId,
            @ApiParam(value = "开始时间",required = true) @RequestParam String startTime,
            @ApiParam(value = "结束时间",required = true) @RequestParam String endTime){
        monthReportService.monthReportDownload(response,organizationId,startTime,endTime);

    }
}
