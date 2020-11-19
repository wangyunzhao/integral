package com.dmsdbj.integral.backstage.provider.service;

import com.dmsdbj.integral.backstage.provider.dao.DaoTest;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author 齐智
 * @Date: 2020/7/25 11:18
 * @Version: 1.0
 **/
@Service
public class ServiceTest {


    @Autowired
    private DaoTest daoTest;

    public String myTest(Integer id)
    {
        String s = daoTest.myTest(id);
        return s;
    }

}
