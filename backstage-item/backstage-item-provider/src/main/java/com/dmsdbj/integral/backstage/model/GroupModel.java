package com.dmsdbj.integral.backstage.model;

import lombok.Data;

import java.util.List;
@Data
public class GroupModel {
     private String name;
     private List<PluginBindsUserModel> userList;
}
