package com.dmsdbj.integral.backstage.model;

import lombok.Data;

import java.util.List;

@Data
public class PluginModel {
    private String englishName;
    private String id;
    private String name;
    private String description;
    private String projectId;
    private String iconUrl;
    private String pageUrl;
    private Integer isEverybody;
    private String userName;
    private List<String> personList;
    private List<String> addPersonList;
    private List<String> deletePersonList;
}
