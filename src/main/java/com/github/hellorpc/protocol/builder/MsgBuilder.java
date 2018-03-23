/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.HelloMsg;
import com.github.hellorpc.util.BcdUtil;
import com.github.hellorpc.util.ByteArrayTool;
import com.github.hellorpc.util.MsgPackException;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class MsgBuilder {
    
    private final HelloMsg msg;
    
    private final Map<MsgType,byte[]> dataMap = new HashMap<MsgType,byte[]>();

    private MsgBuilder() {
        this.msg = new HelloMsg();
    }

    public static MsgBuilder begin() {
        return new MsgBuilder();
    }

    public TpduMarshaler buildTpdu() {
        return new TpduMarshaler(this);
    }

    public MsgBuilder buildTimestamp(long timestamp) throws MsgPackException{
        String ts = String.valueOf(timestamp);
        if(ts.length() != 13 ){
            throw new MsgPackException("## timestamp data length must be 13 size");
        }
        msg.setTimestamp(timestamp);
        byte[] data = BcdUtil.str2Bcd(ts);
        dataMap.put(MsgType.TimeStamp,data);
        return this;
    }
    
    public byte[] pack() throws MsgPackException{
//        if(dataMap.size()!=7){  //数据还没有装配完
//            System.out.println(dataMap.keySet());
//            throw new MsgPackException("## Can't be packaged , incomplete data");
//        }
        byte[] tpdu = dataMap.get(MsgType.TPDU);
        byte[] timeStamp = dataMap.get(MsgType.TimeStamp);
        byte[] header = dataMap.get(MsgType.Header);
        byte[] mti = dataMap.get(MsgType.MTI);
        byte[] bitMap = dataMap.get(MsgType.BitMap);
        byte[] definitedFields = dataMap.get(MsgType.DefinitedFields);
        byte[] msgContent = dataMap.get(MsgType.MsgContent);
        return ByteArrayTool.concat(tpdu,timeStamp,header,mti,bitMap,definitedFields,msgContent);
    }
    
    public HeaderMarshaler buildHeader() {
        return new HeaderMarshaler(this);
    }

    public MtiMarshaler buildMti() {
        return new MtiMarshaler(this);
    }

    public BitMapMarshaler buildBitMap() {
        return new BitMapMarshaler(this);
    }

    public DefinitedFieldsMarshaler buildDefinitedFields() {
        return new DefinitedFieldsMarshaler(this);
    }

    public MsgContentMarshaler buildMsgContent() {
        return new MsgContentMarshaler(this);
    }

    public HelloMsg getMsg() {
        return msg;
    }

    public Map<MsgType, byte[]> getDataMap() {
        return dataMap;
    }

    public HelloMsg end() {
        return msg;
    }

}
