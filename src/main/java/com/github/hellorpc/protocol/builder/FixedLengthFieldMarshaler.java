/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.FixedLengthField;
import com.github.hellorpc.util.BcdUtil;
import com.github.hellorpc.util.DataFormatException;
import java.io.UnsupportedEncodingException;

/**
 * 固定长度域构造器
 *
 * @author George <Georgeinfo@163.com>
 */
public class FixedLengthFieldMarshaler {

    private MsgBuilder msgBuilder;
    private FixedLengthField fixedLengthField;

    public FixedLengthFieldMarshaler() {

    }

    public FixedLengthFieldMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
    }

    public FixedLengthFieldMarshaler addContent(String content) {
        fixedLengthField.setContent(content);
        return this;
    }

    public FixedLengthFieldMarshaler addBcdFlag(boolean isBcd) {
        fixedLengthField.setBcd(isBcd);
        return this;
    }

    public MsgBuilder done() {
        if (fixedLengthField.isBcd()) {
            double len = Math.ceil(fixedLengthField.getContent().length() / 2);
            fixedLengthField.setLength((int) len);
        } else {
            fixedLengthField.setLength(fixedLengthField.getContent().length());
        }
        return msgBuilder;
    }

    public byte[] pack() {
        byte[] data;
        if (fixedLengthField.isBcd()) { // 如果该域指定了使用BCD压缩
            data = BcdUtil.str2Bcd(fixedLengthField.getContent());
        } else {
            try {
                data = fixedLengthField.getContent().getBytes("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new DataFormatException("## String can't convert to utf-8 encoding data.", ex);
            }
        }
        return data;
    }

    public FixedLengthField unpack(byte[] data, boolean isBcd) {
        try {
            FixedLengthField field = new FixedLengthField();
            field.setBcd(isBcd);
            String content;
            if (isBcd) {
                content = BcdUtil.bcd2Str(data);
            } else {
                content = new String(data, "UTF-8");
            }
            field.setContent(content);
            field.setLength(content.length());

            return field;
        } catch (UnsupportedEncodingException ex) {
            throw new DataFormatException("## Data can't convert to utf-8 encoding string.", ex);
        }
    }
}
