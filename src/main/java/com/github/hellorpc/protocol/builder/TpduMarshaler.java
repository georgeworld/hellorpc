/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.TPDU;
import com.github.hellorpc.util.BcdUtil;
import com.github.hellorpc.util.HelloStringUtils;
import com.github.hellorpc.util.MsgPackException;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class TpduMarshaler {

    private final MsgBuilder msgBuilder;
    private final TPDU tpdu;

    public TpduMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
        tpdu = new TPDU();
    }

    public TpduMarshaler addTpduFrom(String from) {
        if (tpdu.getValue() == null) {
            tpdu.setValue(TPDU.PREFIX + from);
        } else {
            tpdu.setValue(HelloStringUtils.replaceSubString(tpdu.getValue(), from, 2, 5));
        }
        return this;
    }

    public TpduMarshaler addTpduTo(String to) {
        if (tpdu.getValue() == null) {
            tpdu.setValue(TPDU.PREFIX + "0000" + to);
        } else {
            if (tpdu.getValue().length() == 10) {
                tpdu.setValue(HelloStringUtils.replaceSubString(tpdu.getValue(), to, 6, 9));
            } else {
                tpdu.setValue(tpdu.getValue() + to);
            }
        }

        return this;
    }

    public byte[] pack() throws MsgPackException {
        if (tpdu == null) {
            throw new MsgPackException("## TPDU object is null.");
        }
        String tpduValue = tpdu.getValue();
        if (tpduValue == null || tpduValue.trim().isEmpty()) {
            throw new MsgPackException("## TPDU value can't be null or empty.");
        }

        if (tpduValue.trim().length() != TPDU.TPDU_LENGTH) {
            throw new MsgPackException("## TPDU value length must be " + TPDU.TPDU_LENGTH + " numbers");
        }

        byte[] data = BcdUtil.str2Bcd(tpduValue.trim());
        if (data.length != (int)Math.ceil(TPDU.TPDU_LENGTH / 2.0)) {
            throw new MsgPackException("## TPDU data length must be " + (TPDU.TPDU_LENGTH / 2.0) + " numbers");
        }

        return data;
    }

    public MsgBuilder done() throws MsgPackException{
        msgBuilder.getMsg().setTpdu(tpdu);
        msgBuilder.getDataMap().put(MsgType.TPDU,pack());
        return msgBuilder;
    }
}
