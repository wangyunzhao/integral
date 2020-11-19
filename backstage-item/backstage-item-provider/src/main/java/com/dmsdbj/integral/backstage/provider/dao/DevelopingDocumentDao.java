package com.dmsdbj.integral.backstage.provider.dao;

import com.dmsdbj.integral.backstage.model.DevelopingDocumentModel;
import com.dmsdbj.integral.backstage.pojo.DevelopingDocumentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/8/4
 * Time: 8:28
 * Description:${DESCRIPTION}
 */
@Repository
public interface DevelopingDocumentDao {

    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/4
    * @time: 8:49
    * @description:查询开发文档
    */
    public DevelopingDocumentModel queryDevelopingDocument();

    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/4
    * @time: 8:51
    * @description:添加开发文档
    */
    public void addDevelopingDocument(DevelopingDocumentEntity developingDocumentModel);
    /*
    * @author: 郝龙飞
    * @version:
    * @param: []
    * @date: 2020/8/4
    * @time: 8:51
    * @description:修改开发文档的状态
    */
    public void updateDevelopingDocument(String id);
}
