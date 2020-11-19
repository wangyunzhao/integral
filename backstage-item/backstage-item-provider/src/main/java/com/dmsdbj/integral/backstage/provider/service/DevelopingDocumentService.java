package com.dmsdbj.integral.backstage.provider.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dmsdbj.cloud.tool.business.IntegralResult;
import com.dmsdbj.integral.backstage.model.DevelopingDocumentModel;
import com.dmsdbj.integral.backstage.pojo.DevelopingDocumentEntity;
import com.dmsdbj.integral.backstage.provider.dao.DevelopingDocumentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/8/4
 * Time: 8:28
 * Description:${DESCRIPTION}
 */
@Service
public class DevelopingDocumentService {

    @Autowired
    private DevelopingDocumentDao developingDocumentDao;

    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/5
    * @time: 11:16
    * @description:查询当前使用中的开发文档
    */
    public IntegralResult queryDevelopingDocument(){
        try {
            DevelopingDocumentModel documentModel=developingDocumentDao.queryDevelopingDocument();
            return IntegralResult.build(IntegralResult.SUCCESS,"查询成功",documentModel);
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"查询失败");
        }
    }

    /*
    * @author: 郝龙飞
    * @version:
    * @param: [id, content, operator]
    * @date: 2020/8/5
    * @time: 11:16
    * @description:添加（修改）开发文档
    */
    @Transactional(rollbackOn = Exception.class)
    public IntegralResult addDevelopingDocument(String id,String content,String operator){
        try {
            //更新原有的开发文档的状态为不可用
            developingDocumentDao.updateDevelopingDocument(id);
            //雪花算法
            String Id= IdWorker.getIdStr();
            DevelopingDocumentEntity documentEntity=new DevelopingDocumentEntity();
            documentEntity.setId(Id);
            documentEntity.setOperator(operator);
            documentEntity.setContent(content);
            developingDocumentDao.addDevelopingDocument(documentEntity);
            return IntegralResult.build(IntegralResult.SUCCESS,"修改成功");
        }catch (Exception e){
            return IntegralResult.build(IntegralResult.FAIL,"修改失败");
        }
    }

}
