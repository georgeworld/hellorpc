/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.entities;

/**
 * HelloRPC报文整体定义
 *
 * @author George <Georgeinfo@163.com>
 */
public class HelloMsg implements java.io.Serializable{

    /**
     * TPDU段
     */
    private TPDU tpdu;
    /**
     * 时间戳段
     */
    private long timestamp;
    /**
     * 报文头段
     */
    private Header header;
    /**
     * 消息类型ID，4个字节的数字字符，
     */
    private String mti;
    /**
     * BitMap，固定含义与开关位图
     */
    private BitMap bitmap;
    /**
     * 固定含义域列表
     */
    private DefinitedField definitedField;
    /**
     * 报文数据内容对象
     */
    private MsgContent msgContent;

    public HelloMsg() {
    }

    public TPDU getTpdu() {
        return tpdu;
    }

    public void setTpdu(TPDU tpdu) {
        this.tpdu = tpdu;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public BitMap getBitmap() {
        if(bitmap==null){
            bitmap = new BitMap();
        }
        return bitmap;
    }

    public void setBitmap(BitMap bitmap) {
        this.bitmap = bitmap;
    }

    public DefinitedField getDefinitedField() {
        return definitedField;
    }

    public void setDefinitedField(DefinitedField definitedField) {
        this.definitedField = definitedField;
    }

    public MsgContent getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(MsgContent msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public String toString() {
        return "HelloMsg{" + "tpdu=" + tpdu + ", timestamp=" + timestamp + ", header=" + header + ", mti=" + mti + ", bitmap=" + bitmap + ", definitedField=" + definitedField + ", msgContent=" + msgContent + '}';
    }
    
}
