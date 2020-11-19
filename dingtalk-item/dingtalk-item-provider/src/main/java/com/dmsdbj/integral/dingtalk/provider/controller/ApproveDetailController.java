package com.dmsdbj.integral.dingtalk.provider.controller;

import com.dingtalk.item.pojo.ApproveDetailEntity;
import com.dmsdbj.integral.dingtalk.provider.service.ApproveDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.text.ParseException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ApproveDetailController接口
 * 根据审批实例id调用钉钉接口获取所有审批存库并返回值
 *
 * @author 王梦瑶
 * @version 0.0.1
 * @since 2020年6月2日08:18:11
 */
@Api(tags = {"根据审批实例得到审批详情"})
@RequestMapping(value = "approveDetail")
@RestController
public class ApproveDetailController {
    @Autowired
    private ApproveDetailService approveDetailService;

    /**
     * 根据审批实例id调用钉钉接口获取所有审批存库并返回值
     *
     * @return
     * @author
     * @since 2019年9月12日17:11:46
     */
    @ApiOperation(value = "根据审批实例id调用钉钉接口获取所有审批存库并返回值")
    @GetMapping(value = {"queryApproveDetail"})
    public List<ApproveDetailEntity> queryApproveDetail(String... processInstanceId) throws ParseException {
        return approveDetailService.queryApproveDetail(processInstanceId);

    }

    /**
     * @return java.util.List<com.dingtalk.item.pojo.ApproveDetailEntity>
     * @Description //查询当天已同意，且请假结束时间小于当前时间（job执行的时间）审批
     * @Param [begin_time] 获取配置文件中的时间（上午默认是当天 8点  ，下午默认是 当天 13点）
     * @Author fjx
     * @Date 2020-06-10 11:24
     **/
    @ApiOperation(value = "获取当天请假人员审批信息（已经同意）")
    @GetMapping(value = {"selectAllApproveDetailAgree"})
    public List<ApproveDetailEntity> selectAllApproveDetailAgree(String begin_time) {
        return approveDetailService.selectAllApproveDetailAgree(begin_time);
    }

}
