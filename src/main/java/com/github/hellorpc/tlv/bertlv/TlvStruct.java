/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.bertlv;

/**
 * TLV数据结构定义
 *
 * @author George <Georgeinfo@163.com>
 */
public class TlvStruct {

    public enum FrameType {
        universal, application, context_specific, private_frame;
    }
    /**
     * 结构体类型
     */
    private FrameType frameType;
    /**
     * 数据元结构是否是复合数据元结构(value部分是否是嵌套TLV结构)
     * 默认内容不是TLV
     */
    private boolean constructedDataFlag = false;
    /**
     * EMV_TLB的纯tag值(不包括tag首字节的类型定义、数据结构及占位bit等)
     */
    private int pureEmvTag = 0;
    /**
     * BER_TLV的纯tag值(不包括tag首字节的类型定义、数据结构及占位bit等)
     */
    private String pureBerTag;
    /**
     * 数据长度
     */
    private int valueLength;
    /**
     * value段的数据
     */
    private byte[] value;

    public TlvStruct() {
    }

    public FrameType getFrameType() {
        return frameType;
    }

    public void setFrameType(FrameType frameType) {
        this.frameType = frameType;
    }

    public boolean isConstructedDataFlag() {
        return constructedDataFlag;
    }

    public void setConstructedDataFlag(boolean constructedDataFlag) {
        this.constructedDataFlag = constructedDataFlag;
    }

    public int getPureEmvTag() {
        return pureEmvTag;
    }

    public void setPureEmvTag(int pureEmvTag) {
        this.pureEmvTag = pureEmvTag;
    }

    public String getPureBerTag() {
        return pureBerTag;
    }

    public void setPureBerTag(String pureBerTag) {
        this.pureBerTag = pureBerTag;
    }

    public int getValueLength() {
        return valueLength;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
        this.valueLength = value.length;
    }

}
