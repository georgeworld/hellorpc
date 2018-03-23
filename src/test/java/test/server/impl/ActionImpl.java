/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package test.server.impl;

import middle.action.ActionApi;
import middle.entities.TestBean;

import java.util.List;
import java.util.Map;


/**
 * 中间定义层定义的动作接口实现类
 *
 * @author dev1
 */
public class ActionImpl implements ActionApi {


    @Override
    public String calc(String a) {
        System.out.println("cal String");
        return a;
    }


    @Override
    public TestBean calc(TestBean a) {
        System.out.println("cal TestBean" + a.getNames());
        return a;
    }

    @Override
    public Map calc(Map a) {
        System.out.println("cal Map" + a);
        return a;
    }

    @Override
    public List calc(List a) {
        System.out.println("cal List" + a);
        return a;
    }

}
