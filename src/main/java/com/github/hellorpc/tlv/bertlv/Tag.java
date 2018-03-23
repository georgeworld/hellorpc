/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.bertlv;

import com.github.hellorpc.util.BinaryTool;
import java.util.Arrays;
import java.util.Objects;

/**
 * TLV的tag包装对象
 *
 * @author George <Georgeinfo@163.com>
 */
public final class Tag {

    /**
     * 整个tag的内容（包括第一个字节的bit8-bit6这三位的类型标识位）<br/>
     * 无论是EMV_TLV格式，还是BER_TLV格式，tagFullData都包括整个tag段的值。
     */
    private byte[] tagFullData;
    /**
     * tag值的字节数组长度，也就是tagFullData的长度
     */
    private int tagFullLength = 0;
    /**
     * EMV精简版TLV格式的tag值（包括第一个字节的bit8-bit6这三位的类型标识位）<br/>
     * 当tag值小于65407(0b11111111
     * 01111111)时，被识别为EMV标准的TLV，当TAG的值超过这个数，则被识别为标准BER_TLV格式。<br/>
     * emvTag其实就是tagFullData转成int类型后的数据
     */
    private int emvTag = 0;
    /**
     * 当TLV格式为BER_TLV标准格式时，纯tag值部分所对应的ASCII字符串，也就是不包括整个第一个字节的tag值，所对应的 ASCII字符串。
     */
    private String berTag;

    public Tag() {
    }

    public Tag(byte[] tagFullData, int emvTag) {
        setTagFullData(tagFullData);
        this.emvTag = emvTag;
    }

    public Tag(byte[] tagFullData, String berTag) {
        setTagFullData(tagFullData);
        this.berTag = berTag;
    }

    public byte[] getTagFullData() {
        return tagFullData;
    }

    public void setTagFullData(byte[] tagFullData) {
        this.tagFullData = tagFullData;
        this.tagFullLength = tagFullData.length;
    }

    public int getEmvTag() {
        return emvTag;
    }

    public void setEmvTag(int emvTag) {
        this.emvTag = emvTag;
    }

    public String getBerTag() {
        if(berTag==null && emvTag != 0){
            byte[] tagbyte = BinaryTool.int2byte(emvTag);
            byte[] tagStr = new byte[tagbyte.length-1];
            System.arraycopy(tagbyte, 1, tagStr, 0, tagStr.length);
            return new String(tagStr);
        }
        return berTag;
    }

    public void setBerTag(String berTag) {
        this.berTag = berTag;
    }

    public int getTagFullLength() {
        return tagFullLength;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Arrays.hashCode(this.tagFullData);
        hash = 83 * hash + this.tagFullLength;
        hash = 83 * hash + this.emvTag;
        hash = 83 * hash + Objects.hashCode(this.berTag);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tag other = (Tag) obj;
        if (this.tagFullLength != other.tagFullLength) {
            return false;
        }
        if (this.emvTag != other.emvTag) {
            return false;
        }
        if (!Objects.equals(this.berTag, other.berTag)) {
            return false;
        }
        if (!Arrays.equals(this.tagFullData, other.tagFullData)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tag{" + "tagFullData=" + tagFullData + ", tagFullLength=" + tagFullLength + ", emvTag=" + emvTag + ", berTag=" + berTag + '}';
    }

}
