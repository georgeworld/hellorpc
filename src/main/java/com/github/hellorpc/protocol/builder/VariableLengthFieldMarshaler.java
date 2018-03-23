/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.VariableLengthField;
import com.github.hellorpc.util.BcdUtil;

/**
 * 变长域封装器
 *
 * @author George <Georgeinfo@163.com>
 */
public class VariableLengthFieldMarshaler {

    private final MsgBuilder msgBuilder;
    private VariableLengthField variableLengthField;

    public VariableLengthFieldMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
        variableLengthField = new VariableLengthField();
    }

    public VariableLengthFieldMarshaler addBcdFlag(boolean isBcd) {
        variableLengthField.setBcd(isBcd);
        return this;
    }

    public VariableLengthFieldMarshaler addContent(byte[] content) {
        variableLengthField.setContent(content);
        return this;
    }
    
    public MsgBuilder done() {
        variableLengthField.setLength(0);
        return msgBuilder;
    }

    public byte[] pack() {
        byte[] data;
        if (variableLengthField.isBcd()) { //如果需要进行 BCD 压缩
            data = BcdUtil.data2Bcd(variableLengthField.getContent());
        } else {
            data = variableLengthField.getContent();
        }
        return data;
    }

    public VariableLengthField unpack(byte[] data, boolean isBcd) {
        VariableLengthField vlf = new VariableLengthField();
        vlf.setBcd(isBcd);
        byte[] rawData;
        if (isBcd) {//如果该域经过了BCD压缩，那么先解压缩
            rawData = BcdUtil.data2Bcd(data);
        } else {
            rawData = data;
        }

        vlf.setContent(rawData);
        vlf.setLength(rawData.length);
        
        return vlf;
    }
}
