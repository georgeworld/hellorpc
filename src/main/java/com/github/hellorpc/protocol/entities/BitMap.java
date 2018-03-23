/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.entities;

import java.util.Arrays;

/**
 * 一个字节的8个bit位的描述类
 *
 * @author George <Georgeinfo@163.com>
 */
public class BitMap {

    /**
     * 位图数据
     */
    private byte data;
    /**
     * bit位开关列表
     */
    private Boolean[] bitSwitchArray = new Boolean[]{false, false, false, false, false, false, false, false};

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public Boolean[] getBitSwitchArray() {
        return bitSwitchArray;
    }

    public void setBitSwitchArray(Boolean[] bitSwitchArray) {
        this.bitSwitchArray = bitSwitchArray;
    }

    @Override
    public String toString() {
        return "BitMap{" + "data=" + data + ", bitSwitchArray=" + Arrays.toString(bitSwitchArray) + '}';
    }
    
    

}
