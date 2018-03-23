/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.entities;

/**
 * 变长域实体类
 *
 * @author George <Georgeinfo@163.com>
 */
public class VariableLengthField {

    private int length;
    private byte[] content;
    private boolean bcd;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public boolean isBcd() {
        return bcd;
    }

    public void setBcd(boolean bcd) {
        this.bcd = bcd;
    }

}
