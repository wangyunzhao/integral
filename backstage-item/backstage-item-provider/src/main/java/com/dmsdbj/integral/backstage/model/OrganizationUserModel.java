package com.dmsdbj.integral.backstage.model;

import lombok.Data;

import java.util.List;

@Data
public class OrganizationUserModel {
   public  Long id;
   public  String name;
   public List<UserListModel> userList;
}
