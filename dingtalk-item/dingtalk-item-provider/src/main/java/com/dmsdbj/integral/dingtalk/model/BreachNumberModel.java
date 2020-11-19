package com.dmsdbj.integral.dingtalk.model;

import com.dingtalk.item.pojo.BreachNumberEntity;
import lombok.Data;

/**
 * @author 马珂
 * @version 1.0
 * @date 2020/8/25 19:56
 * @describe
 */
@Data
public class BreachNumberModel {
    private String userJifenId;
    private int breachNumberSum;
    private BreachNumberEntity breachNumberEntity;
}
