package com.github.hellorpc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hellorpc.container.Container;
import com.github.hellorpc.serialize.HelloRPCSerialize;
import com.github.hellorpc.serialize.HelloRPCSerialize.ObjectType;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 对象序列化工具
 *
 * @author dev1
 */
public class SerializeTool {
    
    private static ObjectMapper mapper = new ObjectMapper();
    
    public static byte[] obj2byte(Object obj) throws IOException {
    	if (obj instanceof Object) {
    		Container c = new Container() ;
    		Object bytes = c.serializa(obj);
    		return mapper.writeValueAsBytes(bytes);
    	}
    	else {
    		return mapper.writeValueAsBytes(obj);
    	}
    }
    
    @SuppressWarnings("unchecked")
	public static Object byte2obj(Class<?> type, byte[] data) throws IOException, ClassNotFoundException, NoSuchFieldException, SecurityException{
    	if (type == Container.class || type == Map.class|| type == List.class) {
			return HelloRPCSerialize.derializable2Obj(mapper.readValue(data,Map.class), type);	
		}
        return mapper.readValue(data,type);
    }

    public static Object byte2obj(byte type, byte[] data) throws IOException, ClassNotFoundException, NoSuchFieldException, SecurityException{
    	Class clazz = ObjectType.getByCode(type).getClazz();
            return byte2obj(clazz, data);
    }

    public static void main(String[] args) throws Exception{
        ObjectMapper om = new ObjectMapper();
        Date d = new Date();
        System.out.println(d.toLocaleString()+" "+d.getTime());
        System.out.println(om.writeValueAsString(d));
        //int[] is = {1,2,3,4,5};
//        int is = 12323;
//        String json = om.writeValueAsString(is);
//        System.out.println(json);
//        System.out.println(om.readValue(json,String.class));
//        HelloMsg hm = new HelloMsg();
//        hm.setTimestamp(System.currentTimeMillis());
//        Date str = new Date();
//        byte[] fb = obj2byte(str);
//        printBit(fb);
//        Object obj = byte2obj(ObjectType.getByClass(str.getClass()).getCode(), fb);
//        System.out.println(obj.getClass());
//        System.out.println(obj);
    }

    public static void printBit(byte[] bb) {
        System.out.println("------------------------------------");
        System.out.println(bb.length);
        for (byte b : bb) {
            String s = Integer.toBinaryString(b);
            if (s.length() > 8) {
                System.out.print("0b"+s.substring(s.length() - 8) + ",");
            } else {
                System.out.print("0b"+StringUtils.leftPad(s, 8, "0") + ",");
            }
        }
        System.out.println("\n------------------------------------");
    }

}
