/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.entities;

/**
 * 固定长度的域
 *
 * @author George <Georgeinfo@163.com>
 */
public class FixedLengthField {

    private int length;
    private String content;
    private boolean bcd;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBcd() {
        return bcd;
    }

    public void setBcd(boolean bcd) {
        this.bcd = bcd;
    }

}
