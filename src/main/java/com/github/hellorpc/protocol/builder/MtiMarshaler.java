/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.util.BcdUtil;
import com.github.hellorpc.util.MsgPackException;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class MtiMarshaler {

    private MsgBuilder msgBuilder;

    public MtiMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
    }
    
    public MtiMarshaler addMti(String mti) throws MsgPackException{
        if(mti.length() != 4){
            throw new MsgPackException("## mti data length must be 4 size");
        }
        msgBuilder.getMsg().setMti(mti);
        return this;
    }

    public MsgBuilder done() {
        byte[] data = BcdUtil.str2Bcd(msgBuilder.getMsg().getMti());
        msgBuilder.getDataMap().put(MsgType.MTI, data);
        return msgBuilder;
    }
}
