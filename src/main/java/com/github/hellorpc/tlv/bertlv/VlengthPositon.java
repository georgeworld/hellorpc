/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.bertlv;

/**
 * value的长度，以及当前整个数据的索引下表值
 *
 * @author George <Georgeinfo@163.com>
 */
public class VlengthPositon {

    private int valueLength;
    private int dataPosition;

    public VlengthPositon(int valueLength, int dataPosition) {
        this.valueLength = valueLength;
        this.dataPosition = dataPosition;
    }

    public int getValueLength() {
        return valueLength;
    }

    public void setValueLength(int valueLength) {
        this.valueLength = valueLength;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public void setDataPosition(int dataPosition) {
        this.dataPosition = dataPosition;
    }
}
