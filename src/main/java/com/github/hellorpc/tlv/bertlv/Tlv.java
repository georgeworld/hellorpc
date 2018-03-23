/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.bertlv;

/**
 * TLV实体类定义
 *
 * @author George <Georgeinfo@163.com>
 */
public class Tlv {

    /**
     * 子域Tag标签
     */
    private Tag tag;

    /**
     * 子域取值的长度
     */
    private int length;

    /**
     * 子域取值
     */
    private byte[] value;

    public Tlv(Tag tag, int length, byte[] value) {
        this.length = length;
        this.tag = tag;
        this.value = value;
    }
    
    public boolean valueIsTlv(){
        byte b = tag.getTagFullData()[0];
        if((b & 0b00100000) == 0b00100000){//tag的第一个byte的第6位是1,则内容也是tlv
            return true;
        }
        return false;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "tag=[" + this.tag + "]," + "length=[" + this.length + "]," + "value=[" + this.value + "]";
    }
}
