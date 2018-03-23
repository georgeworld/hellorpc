package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.BitMap;
import com.github.hellorpc.protocol.entities.DefinitedField;
import com.github.hellorpc.util.BcdUtil;
import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.ByteArrayTool;
import com.github.hellorpc.util.ISOUtil;
import com.github.hellorpc.util.MsgPackException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class DefinitedFieldsMarshaler {

    private final MsgBuilder msgBuilder;
    private final DefinitedField definitedField;
    private final BitMap bitMap;

    public DefinitedFieldsMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
        definitedField = new DefinitedField();
        bitMap = new BitMap();
    }
    
    public DefinitedFieldsMarshaler addDefinitedField0(long timestamp) throws MsgPackException{
        if(String.valueOf(timestamp).length() != 13){
            throw new MsgPackException("## timestamp length must be 13.");
        }
        definitedField.setTimestamp(timestamp);
        bitMap.getBitSwitchArray()[0]=true;
        return this;
    }
    
    public DefinitedFieldsMarshaler addDefinitedField1(String errMsg) throws UnsupportedEncodingException,MsgPackException{
        if(errMsg.getBytes("UTF-8").length > 0xffffff){
            throw new MsgPackException("## errMsg utf-8 code data to lang , max length is 16777215 .");
        }
        definitedField.setErrMsg(errMsg);
        bitMap.getBitSwitchArray()[1]=true;
        return this;
    }
    
    public DefinitedFieldsMarshaler addReqUri(String reqUri) throws MsgPackException{
        if(reqUri.length() > 0xffffff){
            throw new MsgPackException("## errMsg utf-8 code data to lang , max length is 16777215 .");
        }
        definitedField.setReqUri(reqUri);
        bitMap.getBitSwitchArray()[2]=true;
        return this;
    }
    
    public DefinitedFieldsMarshaler addDefinitedField3(byte[] extField) throws MsgPackException{
        if(extField.length > 0xffffff){
            throw new MsgPackException("## errMsg utf-8 code data to lang , max length is 16777215 .");
        }
        definitedField.setExtField3(extField);
        bitMap.getBitSwitchArray()[3]=true;
        return this;
    }
    
    public DefinitedFieldsMarshaler addDefinitedField4(byte[] extField) throws MsgPackException{
        if(extField.length > 0xffffff){
            throw new MsgPackException("## errMsg utf-8 code data to lang , max length is 16777215 .");
        }
        definitedField.setExtField4(extField);
        bitMap.getBitSwitchArray()[4]=true;
        return this;
    }
    
    public DefinitedFieldsMarshaler addDefinitedField5(byte[] extField) throws MsgPackException{
        if(extField.length > 0xffffff){
            throw new MsgPackException("## errMsg utf-8 code data to lang , max length is 16777215 .");
        }
        definitedField.setExtField5(extField);
        bitMap.getBitSwitchArray()[5]=true;
        return this;
    }
    
    public DefinitedFieldsMarshaler addDefinitedField6(byte[] extField) throws MsgPackException{
        if(extField.length > 0xffffff){
            throw new MsgPackException("## errMsg utf-8 code data to lang , max length is 16777215 .");
        }
        definitedField.setExtField6(extField);
        bitMap.getBitSwitchArray()[6]=true;
        return this;
    }
    
    public DefinitedFieldsMarshaler addDefinitedField7(byte[] mac) throws MsgPackException{
        if(mac.length != 8){
            throw new MsgPackException("## mac length must be 8");
        }
        definitedField.setMac(mac);
        bitMap.getBitSwitchArray()[7]=true;
        return this;
    }
    
    public byte[] pack(BitMap bitMap) {
        double num = 0;
        for (int fieldId = 0; fieldId < 8; fieldId++) {
            if (bitMap.getBitSwitchArray()[fieldId] == true) {
                num = (num + Math.pow(2, fieldId));
            }
        }

        byte data = (byte) ((int) num & 0xFF);

        return new byte[]{data};
    }
    
    public byte[] pack() throws MsgPackException,UnsupportedEncodingException {
        if (definitedField == null) {
            throw new MsgPackException("## definitedField object is null.");
        }
        
        byte[] timestampData = null;
        {
            long timestamp = definitedField.getTimestamp();
            if(String.valueOf(timestamp).length() != 13){
                throw new MsgPackException("## timestamp length must be 13.");
            }
            timestampData = BcdUtil.data2Bcd(String.valueOf(timestamp).getBytes());
        }
        byte[] errMsgData = null;
        {
            String errMsg = definitedField.getErrMsg();
            if(errMsg != null && !errMsg.isEmpty()){
                byte[] data = errMsg.getBytes("utf-8");
                byte[] lb = BinaryTool.leftPadZero(BinaryTool.int2byte(data.length), 3);
                errMsgData = ISOUtil.concat(lb, data);
            }
        }
        byte[] extFiled2 = null;
        {
            String uri = definitedField.getReqUri();
            if(uri!=null){
                byte[] data = uri.getBytes("utf-8");
                byte[] lb = BinaryTool.leftPadZero(BinaryTool.int2byte(data.length), 3);
                extFiled2 = ISOUtil.concat(lb, data);
            }
        }
        byte[] extFiled3 = null;
        {
            byte[] data = definitedField.getExtField3();
            if(data!=null){
                byte[] lb = BinaryTool.leftPadZero(BinaryTool.int2byte(data.length), 3);
                extFiled3 = ISOUtil.concat(lb, data);
            }
        }
        byte[] extFiled4 = null;
        {
            byte[] data = definitedField.getExtField4();
            if(data!=null){
                byte[] lb = BinaryTool.leftPadZero(BinaryTool.int2byte(data.length), 3);
                extFiled4 = ISOUtil.concat(lb, data);
            }
        }
        byte[] extFiled5 = null;
        {
            byte[] data = definitedField.getExtField5();
            if(data!=null){
                byte[] lb = BinaryTool.leftPadZero(BinaryTool.int2byte(data.length), 3);
                extFiled5 = ISOUtil.concat(lb, data);
            }
        }
        byte[] extFiled6 = null;
        {
            byte[] data = definitedField.getExtField6();
            if(data!=null){
                byte[] lb = BinaryTool.leftPadZero(BinaryTool.int2byte(data.length), 3);
                extFiled6 = ISOUtil.concat(lb, data);
            }
        }
        byte[] mac = null;
        {
            byte[] data = definitedField.getMac();
            if(data!=null){
                mac = data;
            }
        }
        return ByteArrayTool.concat(timestampData,errMsgData,extFiled2,extFiled3,extFiled4,extFiled5,extFiled6,mac);
    }

    public MsgBuilder done() throws MsgPackException,UnsupportedEncodingException{
        msgBuilder.getMsg().setDefinitedField(definitedField);
        msgBuilder.getDataMap().put(MsgType.DefinitedFields, pack());
        msgBuilder.getMsg().setBitmap(bitMap);
        msgBuilder.getDataMap().put(MsgType.BitMap, pack(bitMap));
        return msgBuilder;
    }
}
