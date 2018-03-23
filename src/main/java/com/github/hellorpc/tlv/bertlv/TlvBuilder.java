/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.bertlv;

import com.github.hellorpc.tlv.bertlv.TlvStruct.FrameType;
import com.github.hellorpc.util.BinaryTool;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class TlvBuilder {

    private final TlvStruct tlvStruct;

    private TlvBuilder() {
        tlvStruct = new TlvStruct();
    }

    public static TlvBuilder begin() {
        return new TlvBuilder();
    }

    public TlvBuilder setFrameType(FrameType frameType) {
        tlvStruct.setFrameType(frameType);
        return this;
    }

    public TlvBuilder setConstructedDataFlag(boolean constructedDataFlag) {
        tlvStruct.setConstructedDataFlag(constructedDataFlag);
        return this;
    }

    public TlvBuilder setPureEmvTag(int pureEmvTag) {
        tlvStruct.setPureEmvTag(pureEmvTag);
        tlvStruct.setPureBerTag(null);
        return this;
    }

    public TlvBuilder setPureBerTag(String pureBerTag) {
        tlvStruct.setPureBerTag(pureBerTag);
        tlvStruct.setPureEmvTag(0);
        return this;
    }

    public TlvBuilder setValue(byte[] value) {
        tlvStruct.setValue(value);
        return this;
    }

    public byte[] end() throws IOException {
        byte firstByte = 0;

        //构建TLV结构类型部分开始
        FrameType frameType = tlvStruct.getFrameType();
        if (frameType == FrameType.application) {
            firstByte = (byte) ((firstByte + 0b01000000) & 0xFF);
        } else if (frameType == FrameType.context_specific) {
            firstByte = (byte) ((firstByte + 0b10000000) & 0xFF);
        } else if (frameType == FrameType.private_frame) {
            firstByte = (byte) ((firstByte + 0b11000000) & 0xFF);
        }

        if (tlvStruct.isConstructedDataFlag()) {
            firstByte = (byte) ((firstByte + 0b00100000) & 0xFF);
        }
        //构建TAG部分开始
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int emvTag = tlvStruct.getPureEmvTag();
        if (emvTag > 0) {//EMV_TLV格式
            if (emvTag <= 0b00011111) {
                firstByte = (byte) ((firstByte + emvTag) & 0xFF);
                baos.write(new byte[]{firstByte});
            } else {
                firstByte = (byte) ((firstByte + 0b00011111) & 0xFF);
                byte emvTagByte = (byte) (emvTag & 0xFF);
                baos.write(new byte[]{firstByte, emvTagByte});
            }
        } else {//BER_TLV格式
            firstByte = (byte) ((firstByte + 0b00011111) & 0xFF);
            baos.write(new byte[]{firstByte});
            byte[] berTag = tlvStruct.getPureBerTag().getBytes("UTF-8");
            int berTagLen = berTag.length;
            for (int i = 0; i < berTagLen; i++) {
                if (i < (berTagLen - 1)) {
                    baos.write(new byte[]{(byte) (berTag[i] + 0b10000000)});
                } else {
                    baos.write(new byte[]{(byte) berTag[i]});
                }
            }
        }
//        byte[] data = baos.toByteArray();
//        baos.close();
        //构建VALUE部分开始
        byte[] value = tlvStruct.getValue();
        int valueLength = value.length;
        //添加TLV数据长度部分
        if (valueLength <= 0b01111111) {
            baos.write(new byte[]{(byte) (valueLength  & 0xFF)});
        } else {
            byte[] valueLenBytes = BinaryTool.int2byte(valueLength);
            baos.write(valueLenBytes.length | 0b10000000);//写入 length的length
            baos.write(valueLenBytes);//写入 length
        }
        //添加TLV  value部分
        baos.write(value);
        
        byte[] allData = baos.toByteArray();
        baos.close();
        return allData;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        byte[] data = TlvBuilder
                .begin().setFrameType(FrameType.context_specific)
                .setConstructedDataFlag(false)
                .setPureBerTag("common.entities")
                .setValue("这里是TLV数据的值".getBytes("utf-8"))
                .end();
        System.err.println(BinaryTool.toBinaryString(data));
    }
}
