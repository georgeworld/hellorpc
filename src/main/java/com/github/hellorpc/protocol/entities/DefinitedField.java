/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.entities;

/**
 * 固定含义域字段
 *
 * @author George <Georgeinfo@163.com>
 */
public class DefinitedField {

    /**
     * Definited Field 0 响应时间戳域
     */
    private long timestamp;

    /**
     * Definited Field 1 错误内容描述域
     */
    private String errMsg;

    /**
     * Definited Field 2 保留扩展域
     */
    private String reqUri;

    /**
     * Definited Field 3 保留扩展域
     */
    private byte[] extField3;

    /**
     * Definited Field 4 保留扩展域
     */
    private byte[] extField4;

    /**
     * Definited Field 5 保留扩展域
     */
    private byte[] extField5;

    /**
     * Definited Field 6 保留扩展域
     */
    private byte[] extField6;
    
    /**
     * Definited Field 7 MAC校验域
     */
    private byte[] mac;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getReqUri() {
        return reqUri;
    }

    public void setReqUri(String reqUri) {
        this.reqUri = reqUri;
    }

    public byte[] getExtField3() {
        return extField3;
    }

    public void setExtField3(byte[] extField3) {
        this.extField3 = extField3;
    }

    public byte[] getExtField4() {
        return extField4;
    }

    public void setExtField4(byte[] extField4) {
        this.extField4 = extField4;
    }

    public byte[] getExtField5() {
        return extField5;
    }

    public void setExtField5(byte[] extField5) {
        this.extField5 = extField5;
    }

    public byte[] getExtField6() {
        return extField6;
    }

    public void setExtField6(byte[] extField6) {
        this.extField6 = extField6;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "DefinitedField{" + "timestamp=" + timestamp + ", errMsg=" + errMsg + ", extField2=" + reqUri + ", extField3=" + extField3 + ", extField4=" + extField4 + ", extField5=" + extField5 + ", extField6=" + extField6 + ", mac=" + mac + '}';
    }
    
}
