/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.entities;

/**
 * TPDU实体类
 *
 * @author George <Georgeinfo@163.com>
 */
public class TPDU {

    /**
     * TPDU的字节长度，默认是10个字节的长度
     */
    public final static int TPDU_LENGTH = 10;
    /**
     * 固定前缀
     */
    public final static String PREFIX = "60";
    
    /**
     * TPDU值（包括PREFIX）
     */
    private String value = null;
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
