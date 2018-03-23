/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package middle.action;

import middle.entities.TestBean;

import java.util.List;
import java.util.Map;

/**
 * 远程过程调用-动作接口定义
 *
 * @author dev1
 */
public interface ActionApi {
    public String calc(String a);

    public TestBean calc(TestBean a);

    public Map calc(Map a);

    public List calc(List a);

}
