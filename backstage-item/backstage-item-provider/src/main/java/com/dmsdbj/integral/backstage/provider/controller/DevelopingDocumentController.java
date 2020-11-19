package com.dmsdbj.integral.backstage.provider.controller;

import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.provider.service.DevelopingDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/8/4
 * Time: 8:27
 * Description:${DESCRIPTION}
 */
@Api(tags={"开发文档接口"})
@RequestMapping(value = "/developingDocument")
@RestController
public class DevelopingDocumentController {

    @Autowired
    private DevelopingDocumentService developingDocumentService;

    @ApiOperation(value = "查询开发文档")
    @GetMapping(value = "queryDevelopingDocument")
    public IntegralResult queryDevelopingDocument(){
        return developingDocumentService.queryDevelopingDocument();
    }

    @ApiOperation(value = "添加（修改）开发文档")
    @PostMapping(value = "addDevelopingDocument")
    public IntegralResult addDevelopingDocument(@RequestParam String id,String content,String operator){
        return developingDocumentService.addDevelopingDocument(id,content,operator);
    }
}
