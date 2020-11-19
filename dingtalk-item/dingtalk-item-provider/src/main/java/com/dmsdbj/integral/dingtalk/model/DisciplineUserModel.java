package com.dmsdbj.integral.dingtalk.model;

import lombok.Data;
/**
 * 获得部门列表（获取当月纪委要用）
 * @Author: 王梦瑶
 * @Description:
 * @Date: Create in 20:44 2020/6/11
 */

@Data
public class DisciplineUserModel {
   private  String name;
   private  String id;
   private  String parentid;
   private  String userId;
}
