package com.github.hellorpc.protocol.parse;

import com.github.hellorpc.container.Container;
import com.github.hellorpc.protocol.entities.BitMap;
import com.github.hellorpc.protocol.entities.DataRule;
import com.github.hellorpc.protocol.entities.DefinitedField;
import com.github.hellorpc.protocol.entities.Header;
import com.github.hellorpc.protocol.entities.HelloMsg;
import com.github.hellorpc.protocol.entities.MsgContent;
import com.github.hellorpc.protocol.entities.ProtocolVersion;
import com.github.hellorpc.protocol.entities.TPDU;
import com.github.hellorpc.serialize.HelloRPCSerialize;
import com.github.hellorpc.serialize.HelloRPCSerialize.ObjectType;
import com.github.hellorpc.tlv.bertlv.Tlv;
import com.github.hellorpc.tlv.bertlv.TlvParser;
import com.github.hellorpc.tlv.vln.VLN;
import com.github.hellorpc.util.BcdUtil;
import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.ByteArrayTool;
import com.github.hellorpc.util.SerializeTool;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dev1
 */
public class MsgParser {
    
    
    public static TPDU parseTpdu(byte[] data){
    	for (byte b:data) {
        	byte b1[] = new byte[1] ;
        	b1[0] = b ;
        }
        TPDU tpdu = new TPDU();
        tpdu.setValue(BcdUtil.bcd2Str(data));
        return tpdu;
    }
    
    public static long parseTimeStamp(byte[] data){
    	for (byte b:data) {
        	byte b1[] = new byte[1] ;
        	b1[0] = b ;
        }
        
        return Long.parseLong(BcdUtil.bcd2Str(data));
    }
    
    public static Header parseHeader(byte[] data){
        int index = 0; //解析data数组,已经被解析的数据的下标
        
        Header header = new Header();
        {//headerField1
            header.setHeaderLength(data.length);
        }
        int headerField1_length = VLN.int2Vln(data.length).length;  //headerField1 长度
        index +=headerField1_length; 
        {//headerField2
            byte headerField2 = data[index];
            index+=1;
            ProtocolVersion pv = new ProtocolVersion();
            pv.setReleased(headerField2>>7== -1);
            Integer verson = headerField2 & 0b01111111;
            pv.setVersion(verson.shortValue());
            header.setProtocolVersion(pv);
        }
        {//headerField3
            int headerField3 = 0;
            byte headerField3_1 = data[index];
            index+=1;
            if(headerField3_1>>7 == -1){//该字节表示 数字长度
                int length = headerField3_1 & 0b01111111;
                byte[] val = new byte[length];
                System.arraycopy(data, index, val, 0, val.length);
                index+=val.length;
                headerField3 = BinaryTool.byte2int(val);
            }else{  //该字节表示数字本身
                headerField3 = headerField3_1 & 0b01111111;
            }
            header.setWholeMsgLength(headerField3);
        }
        {//headerField4
            byte headerField4Data = data[index];
            index+=1;
            DataRule rule = new DataRule();
            rule.setCompression(headerField4Data>>7 == -1);
            rule.setEncryption(0b01000000==(headerField4Data&0b01000000));
            rule.setConnkeepalive((headerField4Data & 0b00100000)==0b00100000);
        }
        {//headerField5
            byte[] headerField5 = new byte[2];
            System.arraycopy(data, index, headerField5, 0, headerField5.length);
            index+=headerField5.length;
            header.setExtendedDataSegment(headerField5);
        }
        {
            //headerField6
            byte[] headerField6 = new byte[data.length-index];
            System.arraycopy(data, index, headerField6, 0, headerField6.length);
            index+=headerField6.length;
            try {
                header.setActionCode(new String(headerField6,"utf-8"));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        if(index==data.length){
            return header;
        }else{
            throw new MsgParseException("data is error,no parse header");
        }
    }
    
    public static String parseMTI(byte[] data){
    	
        String str = BcdUtil.bcd2Str(data);
        return StringUtils.leftPad(str, 4, "0");
    }
    
    public static BitMap parseBitMap(byte data){
    	
        Boolean[] barray = new Boolean[8];
        barray[0] = (data&0b00000001)==0b00000001;
        barray[1] = (data&0b00000010)==0b00000010;
        barray[2] = (data&0b00000100)==0b00000100;
        barray[3] = (data&0b00001000)==0b00001000;
        barray[4] = (data&0b00010000)==0b00010000;
        barray[5] = (data&0b00100000)==0b00100000;
        barray[6] = (data&0b01000000)==0b01000000;
        barray[7] = data>>7 == -1;
        BitMap bm = new BitMap();
        bm.setData(data);
        bm.setBitSwitchArray(barray);
        return bm;
    }
    
    public static DefinitedField parseDefinitedField(BitMap bitMap,byte[] data){
    	
        DefinitedField df = new DefinitedField();
        int index = 0;
        if(bitMap.getBitSwitchArray()[0]){
            byte[] fieldData = new byte[7];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            df.setTimestamp(Long.parseLong(BcdUtil.bcd2Str(fieldData)));
        }
        if(bitMap.getBitSwitchArray()[1]){
            byte[] field_length = new byte[3];
            System.arraycopy(data, index, field_length, 0, field_length.length);
            index+=field_length.length;
            int length = BinaryTool.byte2int(field_length);
            byte[] fieldData = new byte[length];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            try {
                df.setErrMsg(new String(fieldData,"utf-8"));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        if(bitMap.getBitSwitchArray()[2]){
            byte[] field_length = new byte[3];
            System.arraycopy(data, index, field_length, 0, field_length.length);
            index+=field_length.length;
            int length = BinaryTool.byte2int(field_length);
            byte[] fieldData = new byte[length];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            try {
                df.setReqUri(new String(fieldData,"utf-8"));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        if(bitMap.getBitSwitchArray()[3]){
            byte[] field_length = new byte[3];
            System.arraycopy(data, index, field_length, 0, field_length.length);
            index+=field_length.length;
            int length = BinaryTool.byte2int(field_length);
            byte[] fieldData = new byte[length];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            df.setExtField3(fieldData);
        }
        if(bitMap.getBitSwitchArray()[4]){
            byte[] field_length = new byte[3];
            System.arraycopy(data, index, field_length, 0, field_length.length);
            index+=field_length.length;
            int length = BinaryTool.byte2int(field_length);
            byte[] fieldData = new byte[length];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            df.setExtField4(fieldData);
        }
        if(bitMap.getBitSwitchArray()[5]){
            byte[] field_length = new byte[3];
            System.arraycopy(data, index, field_length, 0, field_length.length);
            index+=field_length.length;
            int length = BinaryTool.byte2int(field_length);
            byte[] fieldData = new byte[length];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            df.setExtField5(fieldData);
        }
        if(bitMap.getBitSwitchArray()[6]){
            byte[] field_length = new byte[3];
            System.arraycopy(data, index, field_length, 0, field_length.length);
            index+=field_length.length;
            int length = BinaryTool.byte2int(field_length);
            byte[] fieldData = new byte[length];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            df.setExtField6(fieldData);
        }
        if(bitMap.getBitSwitchArray()[7]){
            byte[] fieldData = new byte[8];
            System.arraycopy(data, index, fieldData, 0, fieldData.length);
            index+=fieldData.length;
            df.setMac(fieldData);
        }
        if(index==data.length){
            return df;
        }else{
            throw new MsgParseException("data is error,no parse header");
        }
    }
    
    public static MsgContent parseMsgContent(byte[] data) throws ClassNotFoundException, NoSuchFieldException, SecurityException{
        try {
        	
            MsgContent mc = new MsgContent();
            Map<String,Tlv> tlvMap = new HashMap<>();
            for(Tlv tlv : TlvParser.buildTlvList(data)){
                tlvMap.put(tlv.getTag().getBerTag(), tlv);
            }
            {//actionClassName 动作类名称
                Tlv tlv = tlvMap.get("actionClassName");
                if(tlv!=null){
                    String actionClassName = (String)SerializeTool.byte2obj(ObjectType.STRING.getCode(),tlv.getValue());
                    mc.setActionClassName(actionClassName);
                }
            }
            {//packagePath 动做类包路径
                Tlv tlv = tlvMap.get("package");
                if(tlv!=null){
                    String packagePath = (String)SerializeTool.byte2obj(ObjectType.STRING.getCode(),tlv.getValue());
                    mc.setPackagePath(packagePath);
                }
            }
            {//methodName 动作方法名称
                Tlv tlv = tlvMap.get("methodName");
                if(tlv!=null){
                    String methodName = (String)SerializeTool.byte2obj(ObjectType.STRING.getCode(),tlv.getValue());
                    mc.setMethodName(methodName);
                }
            }
            {//returnValue 返回值
                Tlv tlv = tlvMap.get("returnType");
                if(tlv!=null){
                    ObjectType ot = ObjectType.getByCode(tlv.getValue()[0]);
                    if(ot.equals(ObjectType.VOID)){
                        mc.setReturnValue(null);
                    }else{
                        mc.setReturnValue(SerializeTool.byte2obj(ot.getCode(),tlvMap.get("returnValue").getValue()));
                    }
                }
            }
            {//params 参数列表
                Tlv ptlv = tlvMap.get("params");
                if(ptlv != null ){
                    byte[] paramsData =  ptlv.getValue();
                    List<Tlv> tlvList = TlvParser.buildTlvList(paramsData);
                    ArrayList<Object> params = new ArrayList<>();
                    ArrayList<Class> paramsType = new ArrayList<>();
                    for(Tlv tlv : tlvList){
                        Map<String,Tlv> param = new HashMap<>();
                        for(Tlv t : TlvParser.buildTlvList(tlv.getValue())){
                            param.put(t.getTag().getBerTag(), t);
                        }
                        ObjectType ot = ObjectType.getByCode(param.get("paramType").getValue()[0]);
                        Object ob = SerializeTool.byte2obj(ot.getCode(),param.get("paramValue").getValue()) ;
                        if (ot.getClazz() == Container.class) {
                        	if (ob instanceof Map) {
                        		paramsType.add(Map.class);
                        	}
                        	else if (ob instanceof List) {
                        		paramsType.add(List.class);
                        	}
                        	else {
                        		paramsType.add(ob.getClass());
                        	}
                        }
                        else {
                        	paramsType.add(ot.getClazz());
                        }
                        params.add(ob);
                    }
                    mc.setMethodParamsList(params);
                    mc.setMethodParamsTypeList(paramsType);
                }
            }
            return mc;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static HelloMsg parseMsg(InputStream is) throws IOException,ClassNotFoundException, NoSuchFieldException, SecurityException {
        long msgLength = 0;//整个报文的长度
        int readLength = 0;//已经解析的报文的长度
        HelloMsg msg = new HelloMsg();
        {//tpdu
            byte[] data = new byte[5];
            is.read(data);
            readLength+=data.length;
            TPDU tpdu = parseTpdu(data);
            msg.setTpdu(tpdu);
        }
        {//timestamp
            byte[] data = new byte[7];
            is.read(data);
            readLength+=data.length;
            long timestamp = parseTimeStamp(data);
            msg.setTimestamp(timestamp);
        }
        {//Header
            byte[] headerData = null;
            //从流中获取header数据
            byte b = (byte)is.read();
            readLength+=1;
            if(b>>7 == 1){//标示 第8个bit是1
                int headerlengthLength = b & 0b0111111;
                byte[] lengthbyte = new byte[headerlengthLength];
                is.read(lengthbyte);
                readLength+=lengthbyte.length;
                int otherheaderlength = BinaryTool.byte2int(lengthbyte) - 1 - headerlengthLength;
                byte[] otherheaderData = new byte[otherheaderlength];
                is.read(otherheaderData);
                readLength+=otherheaderData.length;
                headerData = ByteArrayTool.concat(new byte[]{b},lengthbyte,otherheaderData);
            }else{//直接就是长度
                int otherheaderlength = (b & 0b0111111) -1;
                byte b1[] = new byte[1] ;
            	b1[0] = b ;
                byte[] otherheaderData = new byte[otherheaderlength];
                is.read(otherheaderData);
                readLength+=otherheaderData.length;
                headerData = ByteArrayTool.concat(new byte[]{b},otherheaderData);
            }
            Header header = parseHeader(headerData);
            msgLength = header.getWholeMsgLength();
            msg.setHeader(header);
        }
        {//MTI
            byte[] data = new byte[2];
            is.read(data);
            readLength+=data.length;
            String mit = parseMTI(data);
            msg.setMti(mit);
        }
        {//BitMap
            byte b = (byte)is.read();
            readLength+=1;
            BitMap bm = parseBitMap(b);
            msg.setBitmap(bm);
        }
        {//Definited Fields
            BitMap bitMap = msg.getBitmap();
            DefinitedField df = new DefinitedField();
            if(bitMap.getBitSwitchArray()[0]){
                byte[] fieldData = new byte[7];
                is.read(fieldData);
                readLength+=fieldData.length;
                df.setTimestamp(Long.parseLong(BcdUtil.bcd2Str(fieldData)));
            }
            if(bitMap.getBitSwitchArray()[1]){
                byte[] field_length = new byte[3];
                is.read(field_length);
                readLength+=field_length.length;
                int length = BinaryTool.byte2int(field_length);
                byte[] fieldData = new byte[length];
                is.read(fieldData);
                readLength+=fieldData.length;
                try {
                    df.setErrMsg(new String(fieldData,"utf-8"));
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            if(bitMap.getBitSwitchArray()[2]){
                byte[] field_length = new byte[3];
                is.read(field_length);
                readLength+=field_length.length;
                int length = BinaryTool.byte2int(field_length);
                byte[] fieldData = new byte[length];
                is.read(fieldData);
                readLength+=fieldData.length;
                try {
                    df.setReqUri(new String(fieldData,"utf-8"));
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            if(bitMap.getBitSwitchArray()[3]){
                byte[] field_length = new byte[3];
                is.read(field_length);
                readLength+=field_length.length;
                int length = BinaryTool.byte2int(field_length);
                byte[] fieldData = new byte[length];
                is.read(fieldData);
                readLength+=fieldData.length;
                df.setExtField3(fieldData);
            }
            if(bitMap.getBitSwitchArray()[4]){
                byte[] field_length = new byte[3];
                is.read(field_length);
                readLength+=field_length.length;
                int length = BinaryTool.byte2int(field_length);
                byte[] fieldData = new byte[length];
                is.read(fieldData);
                readLength+=fieldData.length;
                df.setExtField4(fieldData);
            }
            if(bitMap.getBitSwitchArray()[5]){
                byte[] field_length = new byte[3];
                is.read(field_length);
                readLength+=field_length.length;
                int length = BinaryTool.byte2int(field_length);
                byte[] fieldData = new byte[length];
                is.read(fieldData);
                readLength+=fieldData.length;
                df.setExtField5(fieldData);
            }
            if(bitMap.getBitSwitchArray()[6]){
                byte[] field_length = new byte[3];
                is.read(field_length);
                readLength+=field_length.length;
                int length = BinaryTool.byte2int(field_length);
                byte[] fieldData = new byte[length];
                is.read(fieldData);
                readLength+=fieldData.length;
                df.setExtField6(fieldData);
            }
            if(bitMap.getBitSwitchArray()[7]){
                byte[] fieldData = new byte[8];
                is.read(fieldData);
                readLength+=fieldData.length;
                df.setMac(fieldData);
            }
            msg.setDefinitedField(df);
        }
        {//Msg-Content
            byte[] data = new byte[(int)msgLength-readLength];
            is.read(data);
            MsgContent mc = parseMsgContent(data);
            msg.setMsgContent(mc);
        }
        return msg;
    }
    
//    public static void main(String[] args){
//        System.out.println(System.currentTimeMillis()/1000 +" "+Thread.currentThread().getName());
//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("the task is run");
//                System.out.println(System.currentTimeMillis()/1000 +" "+Thread.currentThread().getName());
//            }
//        },new Date(),1000*3);
//    }
}
