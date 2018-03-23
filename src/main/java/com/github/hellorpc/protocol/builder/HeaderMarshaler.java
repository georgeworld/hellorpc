/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.Header;
import com.github.hellorpc.tlv.vln.VLN;
import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.ByteArrayTool;
import com.github.hellorpc.util.DataFormatException;
import com.github.hellorpc.util.MsgPackException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class HeaderMarshaler {

    private final MsgBuilder msgBuilder;
    private Header header;

    public HeaderMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
        this.header = new Header();
    }

//    public HeaderBuilder addHeaderLength(int headerLength) {
//        header.setHeaderLength(headerLength);
//        return this;
//    }
    public HeaderMarshaler addReleasedFlag(boolean released) {
        header.getProtocolVersion().setReleased(released);
        return this;
    }

    public HeaderMarshaler addVersion(short version) {
        header.getProtocolVersion().setVersion(version);
        return this;
    }

    public HeaderMarshaler addWholeMsgLength(long wholeMsgLength) {
        header.setWholeMsgLength(wholeMsgLength);
        return this;
    }

    public HeaderMarshaler addCompressionFlag(boolean compression) {
        header.getDataRule().setCompression(compression);
        return this;
    }

    public HeaderMarshaler addEncryptionFlag(boolean encryption) {
        header.getDataRule().setEncryption(encryption);
        return this;
    }
    public HeaderMarshaler addConnkeepaliveFlag(boolean connkeepalive) {
        header.getDataRule().setConnkeepalive(connkeepalive);
        return this;
    }

    public HeaderMarshaler addExtendedDataSegment(byte extendedDataSegment1, byte extendedDataSegment2) {
        byte[] extendedDataSegment = new byte[2];
        extendedDataSegment[0] = extendedDataSegment1;
        extendedDataSegment[1] = extendedDataSegment2;
        header.setExtendedDataSegment(extendedDataSegment);
        return this;
    }

    public HeaderMarshaler addActionCode(String actionCode) {
        if (actionCode == null || actionCode.trim().isEmpty()) {
            throw new DataFormatException("## Action code can't be null or empty.");
        }
        if (actionCode.trim().getBytes().length > 5) {
            throw new DataFormatException("## The length of action code can not more than 5 bytes.");
        }
        header.setActionCode(actionCode);

        return this;
    }

    public byte[] pack() throws MsgPackException {
        //系列校验 开始
        if (header == null) {
            throw new MsgPackException("## Header object is null.");
        }

        if (header.getProtocolVersion().getVersion() <= 0) {
            throw new MsgPackException("## Header protocol version can't less than 0");
        }

//        if (header.getWholeMsgLength() <= 0) {
//            throw new MsgPackException("## Whole message length can't less than 0");
//        }

        if (header.getExtendedDataSegment() == null
                || header.getExtendedDataSegment().length != 2) {
            throw new MsgPackException("## Header extended data segment must be 2 bytes");
        }

        if (header.getActionCode() == null || header.getActionCode().trim().isEmpty()) {
            throw new MsgPackException("## Action code can't be null or empty.");
        } else {
//            if (header.getActionCode().trim().getBytes().length != 5) {
//                throw new MsgPackException("## Action code must be 5 bytes length.");
//            }
        }
        //系列校验 结束

        //计算报文标识和版本信息
        short version = header.getProtocolVersion().getVersion();
        if (version < 2 || version > 127) {
            throw new MsgPackException("## Protocol version must between 2 and 127");
        }
        int verInt = version & 0x7F;
        int protoVersion;
        boolean isRelease = header.getProtocolVersion().isReleased();
        if (isRelease) {
            protoVersion = 128 + verInt;
        } else {
            protoVersion = verInt;
        }

        //2、报文头标识和版本信息数据
        byte[] verBytes = BinaryTool.int2byte(protoVersion & 0xFF);

        //3、报文总长度
        //byte[] wholeMsgLength = VLN.long2Vln(header.getWholeMsgLength());

        boolean isCompression = header.getDataRule().isCompression();
        boolean isEncryption = header.getDataRule().isEncryption();
        boolean isConnkeeyalive = header.getDataRule().isConnkeepalive();
        int dataRuleValue = 0;
        if (isCompression == true) {
            dataRuleValue = dataRuleValue + 128; //0b10000000
        }
        if (isEncryption == true) {
            dataRuleValue = dataRuleValue + 64;  //0b01000000
        }
        if(isConnkeeyalive == true){
            dataRuleValue = dataRuleValue + 32;  //0b00100000
        }

        //4、报文数据规则
        byte[] dataRule = BinaryTool.int2byte(dataRuleValue & 0xFF);

        //5、备用扩展域
        byte[] extendedDataSegment = header.getExtendedDataSegment();

        //6、应答码子域
        byte[] actionCodeData = null;
        try {
            actionCodeData = header.getActionCode().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //【报文头】除了【整个报文长度】和【报文头长度】两个字节数后的长度
        int headerDataLength_ExceptMsgLengthFieldAndHeaderLengthField = verBytes.length
                //+ wholeMsgLength.length
                + dataRule.length
                + extendedDataSegment.length
                + actionCodeData.length;
        //【整个报文】除了【整个报文长度】和【报文头长度】两个字节数后的长度
        int mesDataLength_ExceptMsgLengthFieldAndHeaderLengthField = 0;
        {
            Map<MsgType,byte[]> dataMap = msgBuilder.getDataMap();
            byte[] tpdu = dataMap.get(MsgType.TPDU);
            byte[] timeStamp = dataMap.get(MsgType.TimeStamp);
            byte[] mti = dataMap.get(MsgType.MTI);
            byte[] bitMap = dataMap.get(MsgType.BitMap);
            byte[] definitedFields = dataMap.get(MsgType.DefinitedFields);
            byte[] msgContent = dataMap.get(MsgType.MsgContent);
            mesDataLength_ExceptMsgLengthFieldAndHeaderLengthField = 
                headerDataLength_ExceptMsgLengthFieldAndHeaderLengthField+
                ByteArrayTool.concat(tpdu,timeStamp,mti,bitMap,definitedFields,msgContent).length;
        }
                 
        
        //计算报文头本身长度
        int headerLength_x = 1;       //先假设报文头长度这个子域本省占1个字节（最常见的报文头长度是： 14个字节）
        int allHeaderDataLength;      //整个报文头数据的长度
        byte[] headerFieldLengthData; //报文头本身长度的数值所占空间
        int msgLength_x = 1;
        int allMsgDataLength;
        byte[] msgLengthFieldLengthData;
        
        int roundLimit = 0;           //循环计数器，在下面的循环计算报文头子域实际数值的过程中，防止出现死循环
        while (true) {//开始循环计算报文头子域实际的长度值
            allHeaderDataLength = headerLength_x + headerDataLength_ExceptMsgLengthFieldAndHeaderLengthField + msgLength_x;
            headerFieldLengthData = VLN.int2Vln(allHeaderDataLength);
            if (headerFieldLengthData.length < headerLength_x) {
                throw new MsgPackException("## Header data length error: header length can't less than 1 byte.");
            }
            boolean bheader = false;
            if (headerFieldLengthData.length > headerLength_x) {
                headerLength_x++;
            }else{
                bheader = true;
            }
            
            allMsgDataLength = headerLength_x + mesDataLength_ExceptMsgLengthFieldAndHeaderLengthField + msgLength_x;
            msgLengthFieldLengthData = VLN.int2Vln(allMsgDataLength);
            if (msgLengthFieldLengthData.length < msgLength_x) {
                throw new MsgPackException("## msg data length error: msg length can't less than 1 byte.");
            }
            boolean bmsg = false;
            if(msgLengthFieldLengthData.length > msgLength_x){
                msgLength_x++;
            }else{
                bmsg = true;
            }
            if(bheader && bmsg){
                break;
            }
            
            if (roundLimit >= 7) {//如果循环超过了8轮，则强制退出循环，防止出现死循环
                throw new MsgPackException("## Header length field itself can't over than " + Long.MAX_VALUE + " (8 bytes).");
            }else{
                roundLimit++;
            }
        }

        this.header.setHeaderLength(allHeaderDataLength);
        this.header.setWholeMsgLength(allMsgDataLength);
        return ByteArrayTool.concat(headerFieldLengthData,verBytes,msgLengthFieldLengthData,dataRule,extendedDataSegment,header.getActionCode().getBytes());
    }

    public MsgBuilder done() throws MsgPackException{
        msgBuilder.getMsg().setHeader(header);
        msgBuilder.getDataMap().put(MsgType.Header, pack());
        return msgBuilder;
    }

    public static void main(String[] args) {
        byte[] baData;
        try {
            baData = new HeaderMarshaler(null)
                    .addReleasedFlag(true)
                    .addVersion(Short.valueOf("8"))
                    .addWholeMsgLength(Short.MAX_VALUE)
                    .addCompressionFlag(true)
                    .addEncryptionFlag(true)
                    .addExtendedDataSegment((byte) 0x00, (byte) 0x00)
                    .addActionCode("00000").pack();

            System.out.println("## 报文头数据字节数："+baData.length);
            System.err.println(BinaryTool.toBinaryString(baData));
        } catch (MsgPackException ex) {
            ex.printStackTrace();
        }

    }
}
