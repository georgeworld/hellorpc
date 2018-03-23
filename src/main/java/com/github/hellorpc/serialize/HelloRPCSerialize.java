package com.github.hellorpc.serialize;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hellorpc.container.ClassContainner;
import com.github.hellorpc.container.Container;
import com.github.hellorpc.container.MapContainer;
import com.github.hellorpc.tlv.Lv;
import com.github.hellorpc.tlv.bertlv.Tlv;
import com.github.hellorpc.tlv.bertlv.TlvBuilder;
import com.github.hellorpc.tlv.bertlv.TlvParser;
import com.github.hellorpc.tlv.bertlv.TlvStruct;
import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.ByteArrayTool;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * HelloRPC 序列化
 *
 * @author dev1 在序列化的时候最下面一层的TLV的value的第一个字节是表示它是：数组、list、set、map、什么都不是。
 * 如果是Map则后两个字节第一个表示 key的类型，第二个字节表示value的类型
 */
public class HelloRPCSerialize implements java.io.Serializable {

    /**
     * 交互的时候可以传输的数据格式。
     */
    public enum ObjectType {
        //基本类型
        BYTE((byte) 0b00000001, byte.class),
        SHORT((byte) 0b00000010, short.class),
        INT((byte) 0b00000011, int.class),
        LONG((byte) 0b00000100, long.class),
        FLOAT((byte) 0b00000101, float.class),
        DOUBLE((byte) 0b00000110, double.class),
        CHAR((byte) 0b00000111, char.class),
        BOOLEAN((byte) 0b00001000, boolean.class),
        VOID((byte) 0b00001100, null),//无返回值
        STRING((byte) 0b00001001, String.class), //字符串也是基本类型
        DATE((byte) 0b00001010, Date.class), //时间也是基本类型
        OBJECT((byte) 0b00001011, Object.class),//对象类型
        ARRAY((byte) 0b00001101, Array.class), //数组
        LIST((byte) 0b00001110, List.class), //List
        SET((byte) 0b00001111, Set.class), //Set
        MAP((byte) 0b00010000, Map.class), //Map
        STRINGS((byte) 0b00010001, String[].class), //String[]
        CONTAINER((byte) 0b00010010, Container.class), //CONTAINER
        ;
        private final byte code;

        private final Class clazz;

        public byte getCode() {
            return code;
        }

        public Class getClazz() {
            return clazz;
        }

        private ObjectType(byte code, Class clazz) {
            this.code = code;
            this.clazz = clazz;
        }

        public static ObjectType getByClass(Class clazz) {
            if (clazz == Integer.class) {
                return INT;
            } else if (clazz == Byte.class) {
                return BYTE;
            } else if (clazz == Short.class) {
                return SHORT;
            } else if (clazz == Long.class) {
                return LONG;
            } else if (clazz == Float.class) {
                return FLOAT;
            } else if (clazz == Double.class) {
                return DOUBLE;
            } else if (clazz == Character.class) {
                return CHAR;
            } else if (clazz == Boolean.class) {
                return BOOLEAN;
            } else if (clazz == String.class) {
            	return STRING;
            } else if (clazz == Date.class) {
            	return DATE;
            }
//            for (ObjectType tv : ObjectType.values()) {
//                if (tv.getClazz() == clazz) {
//                	System.out.println("clazz:"+clazz);
//                	System.out.println("clazz:"+tv.getClazz());
//                    return tv;
//                }
//            }
            return CONTAINER;
        }

        public static ObjectType getByCode(byte code) {
            for (ObjectType tv : ObjectType.values()) {
                if (tv.getCode() == code) {
                    return tv;
                }
            }
            return OBJECT;
        }

    }

    /**
     * 是否是一个RPC基本类型 false:不是、true:是
     */
    private static boolean isBasicType(Object obj) {
        Class clazz = obj.getClass();
        if (clazz == byte.class || clazz == Byte.class
                || clazz == char.class || clazz == Character.class
                || clazz == short.class || clazz == Short.class
                || clazz == int.class || clazz == Integer.class
                || clazz == long.class || clazz == Long.class
                || clazz == float.class || clazz == Float.class
                || clazz == double.class || clazz == Double.class
                || clazz == boolean.class || clazz == Boolean.class
                || //下面这些 特殊处理 也当初基本类型
                clazz == String.class || clazz == Date.class) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个RPC数组类型 false:不是、true:是
     */
    private static boolean isArrayOrListOrSet(Object obj) {
        Class clazz = obj.getClass();
        boolean array = clazz.isArray();
        if (array) {
            return true;
        }
        Class[] cs = clazz.getInterfaces();
        if (cs != null) {
            for (Class c : cs) {
                if (c == List.class | c == Set.class) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isList(Object obj) {
        Class clazz = obj.getClass();
        Class[] cs = clazz.getInterfaces();
        if (cs != null) {
            for (Class c : cs) {
                if (c == List.class) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isSet(Object obj) {
        Class clazz = obj.getClass();
        Class[] cs = clazz.getInterfaces();
        if (cs != null) {
            for (Class c : cs) {
                if (c == Set.class) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是一个RPC Map类型 false:不是、true:是
     */
    private static boolean isMap(Object obj) {
        Class[] cs = obj.getClass().getInterfaces();
        if (cs != null) {
            for (Class c : cs) {
                if (c == Map.class) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将进本类型以及包装类型，和Date，String 转bytes
     *
     * @param obj 转换对象
     * @param tagName tlv的tag，默认：param
     */
    private static byte[] serializableBasicType2bytes(Object obj, String tagName) throws IOException {
        if (obj == null) {
            return null;
        }
        Class clazz = obj.getClass();
        ObjectType ot = ObjectType.getByClass(clazz);
        String value = null;
        if (clazz == Date.class) {//日起 特殊处理
            Date d = (Date) obj;
            value = String.valueOf(d.getTime());
        } else {
            value = String.valueOf(obj);
        }
        byte[] tlvValue = ByteArrayTool.concat(new byte[]{ot.getCode()}, value.getBytes());
        byte[] values = null;
        if (tagName == null) {
            values = Lv.data2Lv(tlvValue);
        } else {
            values = TlvBuilder.begin().setPureBerTag(tagName).setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.universal).setValue(tlvValue).end();
        }
        return values;
    }

    /**
     * 将对象类型转换为byte数组
     */
    public static byte[] serializableObj2bytes(Object obj, String tagName) throws IOException, IllegalArgumentException, IllegalAccessException {
        if (obj == null) {
            return null;
        }
        byte[] tlvValue = null;
        Class clazz = obj.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            String filedName = f.getName();
            f.setAccessible(true);
            Object val = f.get(obj);
            if (val != null) {
                if (isBasicType(val)) {
                    tlvValue = ByteArrayTool.concat(tlvValue, serializableBasicType2bytes(val, filedName));
                } else if (isArrayOrListOrSet(val)) {
                    tlvValue = ByteArrayTool.concat(tlvValue, serializableArray2bytes(val, filedName));
                } else if (isMap(val)) {
                    tlvValue = ByteArrayTool.concat(tlvValue, serializableMap2bytes(val, filedName));
                } else {
                    tlvValue = ByteArrayTool.concat(tlvValue, serializableObj2bytes(val, filedName));
                }
            }
        }
        tlvValue = ByteArrayTool.concat(new byte[]{ObjectType.OBJECT.getCode()}, Lv.data2Lv(clazz.getName().getBytes()), tlvValue);
//        System.out.println(tlvValue.length + "," + tagName + "=" + BinaryTool.toBinaryString(tlvValue));
        
        byte[] values;
        if (tagName == null) {
            values = Lv.data2Lv(tlvValue);
        } else {
            values = TlvBuilder.begin().setPureBerTag(tagName).setConstructedDataFlag(true).setFrameType(TlvStruct.FrameType.universal).setValue(tlvValue).end();
        }
        return values;
    }

    /**
     * 将数组类型转换为byte数组
     */
    private static byte[] serializableArray2bytes(Object obj, String tagName) throws IOException, IllegalArgumentException, IllegalAccessException {
        if (obj == null) {
            return null;
        }
        Object[] objs = null;
        byte[] tlvValue = null;
        if (obj.getClass().isArray()) {
            objs = (Object[]) obj;
            tlvValue = new byte[]{ObjectType.ARRAY.getCode()};
        } else if (isList(obj)) {
            List lobj = (List) obj;
            objs = lobj.toArray();
            tlvValue = new byte[]{ObjectType.LIST.getCode()};
        } else if (isSet(obj)) {
            Set sobj = (Set) obj;
            objs = sobj.toArray();
            tlvValue = new byte[]{ObjectType.SET.getCode()};
        }
        if (objs.length < 1) {
            return null;
        }
        for (int i = 0; i < objs.length; i++) {
            Object o = objs[i];
            if (isBasicType(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableBasicType2bytes(o, i + ""));
            } else if (isArrayOrListOrSet(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableArray2bytes(o, i + ""));
            } else if (isMap(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableMap2bytes(o, i + ""));
            } else {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableObj2bytes(0, i + ""));
            }
        }

        byte[] value = null;
        if (tagName == null) {
            value = Lv.data2Lv(tlvValue);
        } else {
            value = TlvBuilder.begin().setPureBerTag(tagName).setConstructedDataFlag(true).setFrameType(TlvStruct.FrameType.universal).setValue(tlvValue).end();
        }
        return value;
    }

    /**
     * 将Map类型转换为byte数组
     */
    private static byte[] serializableMap2bytes(Object obj, String tagName) throws IOException, IllegalArgumentException, IllegalAccessException {
        if (obj == null) {
            return null;
        }
//        tagName = tagName==null?"param":tagName;
        Map<String, Object> map = (Map<String, Object>) obj;
        byte[] tlvValue = new byte[]{ObjectType.MAP.getCode()};;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            System.out.println("tlvValue="+Arrays.toString(tlvValue));
            Object o = entry.getValue();
            String tag = entry.getKey();
            if (isBasicType(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableBasicType2bytes(o, tag));
            } else if (isArrayOrListOrSet(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableArray2bytes(o, tag));
            } else if (isMap(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableMap2bytes(o, tag));
            } else {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableObj2bytes(o, tag));
            }
        }
        byte[] value = null;
        if (tagName == null) {
            value = Lv.data2Lv(tlvValue);
        } else {
            value = TlvBuilder.begin().setPureBerTag(tagName).setConstructedDataFlag(true).setFrameType(TlvStruct.FrameType.universal).setValue(tlvValue).end();
        }
        return value;
    }

    public static byte[] obj2bytes(Object o) {
        if (o == null) {
            return null;
        }
        byte[] tlvValue = null;
        try {
            if (isBasicType(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableBasicType2bytes(o, null));
            } else if (isArrayOrListOrSet(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableArray2bytes(o, null));
            } else if (isMap(o)) {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableMap2bytes(o, null));
            } else {
                tlvValue = ByteArrayTool.concat(tlvValue, serializableObj2bytes(o, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tlvValue;
    }

    /**
     * ******************接下来的方法是反序列化 ********************************
     */
    /**
     * 是否是一个RPC基本类型 false:不是、true:是
     */
    private static boolean basicType(byte type) {

        if (type == ObjectType.BYTE.getCode()
                || type == ObjectType.SHORT.getCode()
                || type == ObjectType.INT.getCode()
                || type == ObjectType.LONG.getCode()
                || type == ObjectType.FLOAT.getCode()
                || type == ObjectType.DOUBLE.getCode()
                || type == ObjectType.BOOLEAN.getCode()
                || type == ObjectType.CHAR.getCode()
                || type == ObjectType.DATE.getCode()
                || type == ObjectType.STRING.getCode()) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个RPC数组类型 false:不是、true:是
     */
    private static boolean array(byte type) {
        if (ObjectType.ARRAY.getCode() == type) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个RPC List类型 false:不是、true:是
     */
    private static boolean list(byte type) {
        if (ObjectType.LIST.getCode() == type) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个RPC List类型 false:不是、true:是
     */
    private static boolean set(byte type) {
        if (ObjectType.SET.getCode() == type) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个RPC Map类型 false:不是、true:是
     */
    private static boolean map(byte type) {
        if (ObjectType.MAP.getCode() == type) {
            return true;
        }
        return false;
    }

    /**
     * 是否是一个RPC 对象类型 false:不是、true:是
     */
    private static boolean object(byte type) {
        if (ObjectType.OBJECT.getCode() == type) {
            return true;
        }
        return false;
    }

    /**
     * 基本类型的转换
     */
    private static Object deserializationBasicType(byte[] bytes) {
        byte b1 = bytes[0];
        byte[] other = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, other, 0, other.length);
        String otherstr = null;
        try {
            otherstr = new String(other);
        } catch (Exception e) {
            System.out.println("new String error;");
        }
        if (b1 == ObjectType.BYTE.getCode()) {
            return Byte.valueOf(otherstr);
        } else if (b1 == ObjectType.SHORT.getCode()) {
            return Short.valueOf(otherstr);
        } else if (b1 == ObjectType.INT.getCode()) {
            return Integer.valueOf(otherstr);
        } else if (b1 == ObjectType.LONG.getCode()) {
            return Long.valueOf(otherstr);
        } else if (b1 == ObjectType.FLOAT.getCode()) {
            return Float.valueOf(otherstr);
        } else if (b1 == ObjectType.DOUBLE.getCode()) {
            return Double.valueOf(otherstr);
        } else if (b1 == ObjectType.BOOLEAN.getCode()) {
            return Boolean.valueOf(otherstr);
        } else if (b1 == ObjectType.CHAR.getCode()) {//字符
            return otherstr.toCharArray()[0];
        } else if (b1 == ObjectType.STRING.getCode()) {
            return otherstr;
        } else if (b1 == ObjectType.DATE.getCode()) {
            Long timestamp = Long.valueOf(otherstr);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            return cal.getTime();
        }
        return null;
    }

    /**
     * 对象类型的转换
     */
    private static Object deserializationObject(byte[] bytes) {
        int index = 0; //这个用来模拟流来read数据的时候使用
        byte type = bytes[index]; //这个byte在这里不在读取， 进到这个方法里面就代表这个byte已经知道了
        index++;
        //下面开始解析lv来获得对象的类路径【包路径+类名】 开始
        String className = null;
        byte lv1byte = bytes[index];
        index++;
        if ((lv1byte >> 7) == 1) {//说明第八个bit位是1，则这个byte的余下7个bit的数据是lv的长度的长度
            int value_length_length = lv1byte & 0b01111111;
            byte[] value_length_data = new byte[value_length_length];
            System.arraycopy(bytes, index, value_length_data, 0, value_length_data.length);
            index += value_length_data.length;
            int value_length = BinaryTool.byte2int(value_length_data);
            byte[] value_data = new byte[value_length];
            System.arraycopy(bytes, index, value_data, 0, value_data.length);
            index += value_data.length;
            className = new String(value_data);
        } else {//第一个byte的 余下的7个bit位的数据就是lv的数据的长度
            int value_length = lv1byte & 0b01111111;
            byte[] value_data = new byte[value_length];
            System.arraycopy(bytes, index, value_data, 0, value_data.length);
            index += value_data.length;
            className = new String(value_data);
        }
        //下面开始解析lv来获得对象的类路径【包路径+类名】 结束
        byte[] tlvs_data = new byte[bytes.length - index];
        System.arraycopy(bytes, index, tlvs_data, 0, tlvs_data.length);
        //-----------到这里的时候 byte数组中的数据已经全部拆分完毕
        try {
            Class returnObjectClass = Class.forName(className);
            Object returnObject = returnObjectClass.newInstance();
            
            List<Tlv> tlvs = TlvParser.buildTlvList(tlvs_data);
            for (Tlv tlv : tlvs) {
                String fieldName = tlv.getTag().getBerTag();
                try {
                    Field field = returnObjectClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    byte[] values = tlv.getValue();
                    byte typeByte = values[0];
                    if (basicType(typeByte)) {
                        field.set(returnObject, deserializationBasicType(values));
                    } else if (object(typeByte)) {
                        field.set(returnObject, deserializationObject(values));
                    } else if (array(typeByte)) {
                        field.set(returnObject, deserializationArray(values));
                    } else if (list(typeByte)) {
                        field.set(returnObject, deserializationList(values));
                    } else if (set(typeByte)) {
                        field.set(returnObject, deserializationSet(values));
                    } else if (map(typeByte)) {
                        field.set(returnObject, deserializationMap(values));
                    }
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException("### the class :" + className + " is not have a field , the name :" + fieldName, ex);
                }
            }
            return returnObject;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    /**
     * array类型的转换
     */
    private static Object deserializationArray(byte[] bytes) {

        return ((List) deserializationList(bytes)).toArray();
    }

    /**
     * List类型的转换
     */
    private static Object deserializationList(byte[] bytes) {
//        byte typeByte2 = bytes[0];//这个在这里已经 没有用
        byte[] list_data = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, list_data, 0, list_data.length);
        List<Object> list = new ArrayList<>();
        try {
            List<Tlv> tlvList = TlvParser.buildTlvList(list_data);
            for (Tlv tlv : tlvList) {
                String fieldName = tlv.getTag().getBerTag();
                byte[] values = tlv.getValue();
                byte typeByte = values[0];
                if (basicType(typeByte)) {
                    list.add(deserializationBasicType(values));
                } else if (object(typeByte)) {
                    list.add(deserializationObject(values));
                } else if (array(typeByte)) {
                    list.add(deserializationArray(values));
                } else if (list(typeByte)) {
                    list.add(deserializationList(values));
                } else if (set(typeByte)) {
                    list.add(deserializationSet(values));
                } else if (map(typeByte)) {
                    list.add(deserializationMap(values));
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set类型的转换
     */
    private static Object deserializationSet(byte[] bytes) {
        List list = (List) deserializationList(bytes);
        Set set = new LinkedHashSet();
        set.addAll(list);
        return set;
    }

    /**
     * Map类型的转换
     */
    private static Object deserializationMap(byte[] bytes) {
//        byte typeByte2 = bytes[0];//这个在这里已经 没有用
        byte[] map_data = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, map_data, 0, map_data.length);
        Map<String, Object> map = new LinkedHashMap<>();
        try {
//            System.out.println("mapData=" + Arrays.toString(map_data));
            List<Tlv> tlvList = TlvParser.buildTlvList(map_data);
            for (Tlv tlv : tlvList) {
                String key = tlv.getTag().getBerTag();
                byte[] values = tlv.getValue();
                byte typeByte = values[0];
                if (basicType(typeByte)) {
                    map.put(key, deserializationBasicType(values));
                } else if (object(typeByte)) {
                    map.put(key, deserializationObject(values));
                } else if (array(typeByte)) {
                    map.put(key, deserializationArray(values));
                } else if (list(typeByte)) {
                    map.put(key, deserializationList(values));
                } else if (set(typeByte)) {
                    map.put(key, deserializationSet(values));
                } else if (map(typeByte)) {
                    map.put(key, deserializationMap(values));
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object bytes2obj(byte[] bytes) {
        List<byte[]> lvs = Lv.lvs2Datas(bytes);
        if (lvs == null || lvs.isEmpty()) {
            throw new RuntimeException("the data is not helloRPC serializable data.");
        }
        byte[] lv = lvs.get(0);
        byte typeByte = lv[0];
        byte[] obj_data = lv;
        if (basicType(typeByte)) {
            return deserializationBasicType(obj_data);
        } else if (object(typeByte)) {
            return deserializationObject(obj_data);
        } else if (array(typeByte)) {
            return deserializationArray(obj_data);
        } else if (list(typeByte)) {
            return deserializationList(obj_data);
        } else if (set(typeByte)) {
            return deserializationSet(obj_data);
        } else if (map(typeByte)) {
            return deserializationMap(obj_data);
        }
        return null;
    }
    

    
//    public static void main(String[] args)throws Exception{
//        
//    }
    /**
     * GL add 2016.5
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     * @throws ClassNotFoundException 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     */
    public static Object derializable2Obj(Map data,Class<?> type) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException, NoSuchFieldException, SecurityException{
    	
    	if (type == Container.class || type == Map.class|| type == List.class) {
    		return derializablebytes2Obj(data,type) ;
    	}
    	
    	return null ;
    }
    
    
    public static Map serializablebytes2Map (Map map,Class<?> type) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
    	
		Map _valuemap = (Map) map.get("_valuemap");
		String _keytype = (String) map.get("_keytype");
		String _valuetype = (String) map.get("_valuetype");
		Class keytype = Class.forName(_keytype);
		Class valuetype = Class.forName(_valuetype);
		
		if ((keytype == String.class)
				||(keytype == int.class)
				||(keytype == float.class)
				||(keytype == double.class)) {
			
			@SuppressWarnings("rawtypes")
			MapContainer mc = new MapContainer();
			
			mc._keytype = keytype.getClass();
			if ((valuetype == String.class)
					||(valuetype == int.class)
    				||(valuetype == float.class)
    				||(valuetype == double.class)) {
				return _valuemap ;
				
			}else if(valuetype == ClassContainner.class){
				
				Field vals[] = valuetype.getFields();
				Map resultmap = new HashMap();
				Set set = _valuemap.entrySet() ;
				Iterator it = set.iterator();
				while (it.hasNext()) {
					Entry entry = (Entry) it.next();
					Object key = entry.getKey();
					Map val = (Map) entry.getValue();
					//val为对象
					try {
						Object obj = valuetype.newInstance();
						for (Field f : vals) {
							f.set(obj, val.get(f.getName()));
						}
						resultmap.put(key, obj);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
				}
//				for (Field f : vals) {
//					f.set(valuetype, value);;
//				}
				return resultmap ;
				
			}
		}
		else {
			
		}
		
	
    	
    	return null ;
    }
    
    static ObjectMapper mapper = new ObjectMapper();
    @SuppressWarnings({ "rawtypes", "unused" })
	public static Object derializablebytes2Obj (Map map,Class<?> type) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException, NoSuchFieldException, SecurityException {
    	if (map == null ) {
    		return null ;
    	}
    	String c = (String) map.get("_c");
    	if (c == null) {
    		return null ;
    	}
    	Class objt = Class.forName(c) ;
    	Object objthis = null ;
    	try {
    		if (objt == Map.class) {
    			objthis = new HashMap();
    		}
    		else if (objt == List.class) {
    			objthis = new ArrayList();
    		}
    		else {
    			objthis = objt.newInstance();
    		}
			Map v = (Map) map.get("_v");
			Map kt = null ;
			if (map.get("_kt")==null || "".equals(map.get("_kt"))) {
				kt = (Map) map.get("_kt");
			}
			Map vt = (Map) map.get("_vt");
			//取当前对象val
			Set valset = v.entrySet() ;
			Iterator valit = valset.iterator();
			while (valit.hasNext()) {
				Entry entry = (Entry) valit.next() ;
				Object key = entry.getKey() ;
				Object val = entry.getValue();
				if (val == null) {
					continue ;
				}
				Object vtype = vt.get(key);
				if ("string".equals(vtype)) {
					vtype = String.class.getName() ;
				}
				Class vtypeClass = Class.forName((String) vtype) ;
				if (objthis instanceof Map) {
					if (Container.isBasicType(vtypeClass)) {
						((Map)objthis).put(key, val) ;
					}
					else {
						((Map)objthis).put(key, derializablebytes2Obj((Map) val,null)) ;
					}
				} 
				else if (objthis instanceof List) {
					if (Container.isBasicType(vtypeClass)) {
						((List)objthis).add(val) ;
					}
					else {
						((List)objthis).add( derializablebytes2Obj((Map) val,null)) ;
					}
				}
				else if(objthis instanceof Object) {
				
					//取当前对象 field
					Field field = objt.getField((String) key) ;
					
					if (Container.isBasicType(vtypeClass)) {
						field.set(objthis, val);
					}
					else {
						field.set(objthis, derializablebytes2Obj((Map) val,null));
					}
				}
				
				
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	
    	
		return objthis ;
		
	}

}
