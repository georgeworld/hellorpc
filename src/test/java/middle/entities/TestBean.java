/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package middle.entities;

import com.github.hellorpc.serialize.HelloRPCSerialize;

/**
 * 测试数据载体类
 */
public class TestBean extends HelloRPCSerialize implements FatherBean {

    public String name;
    public TestBean names;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestBean getNames() {
        return names;
    }

    public void setNames(TestBean names) {
        this.names = names;
    }


}
