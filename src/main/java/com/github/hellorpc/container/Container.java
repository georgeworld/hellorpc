package com.github.hellorpc.container;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Container {
	
	public Map _v = null ;
	public Map _kt = null ;
	public Map _vt = null ;
	public Class _c = null ;
	
	public Object serializa(Object obj) {
		_c = obj.getClass() ;
		Class clazz = obj.getClass() ;
		Map map = new HashMap();
		try {
			if (isBasicType(clazz)) {
				return obj;
			}
			else if (obj instanceof Map) {
				
				obj = serializableMap((Map) obj , null);
				
			}
			else if (obj instanceof List) {
				
				obj = serializableList((List) obj , null);
				
			}
			else if (obj instanceof Object) {
				
				obj = serializableObj( obj,null);
				
			}
			else {
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return obj ;
	
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public Object serializableMap(Map map, Container c) throws JsonProcessingException, IllegalArgumentException, IllegalAccessException, InstantiationException{
		
		if (map == null) {
			throw new RuntimeException("map can not be null!");
		}
		Container container = null ;
		if (c == null) {
			container = this ;
		}
		else {
			container = c;
		}
		
		container._c = map.getClass() ;
		container._v = new HashMap() ;
		container._kt = new HashMap() ;
		container._vt = new HashMap() ;
		
		Set<Entry> entrys = map.entrySet() ;
		Iterator<Entry> it = entrys.iterator();
		
		while (it.hasNext()) {
			Entry entry = (Entry) it.next() ;
			Object k = entry.getKey();
			Object v = entry.getValue();
			_kt.put(k, k.getClass()) ;
			if (v instanceof Map) {
				_v.put(k, serializableMap((Map) v , new Container()) ); 
				_vt.put(k, v.getClass()) ;
			}
			else if (v instanceof Object && !(v instanceof String) && !(v instanceof Date)) {
				_v.put(k, serializableObj(v,new Container())); 
				_vt.put(k, v.getClass()) ;
			}
			else {//把基础类型放到_valuemap
				_v.put(k,v);
				_vt.put(k, v.getClass()) ;
			}
		}
		return container;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public Object serializableList(List list, Container c) throws JsonProcessingException, IllegalArgumentException, IllegalAccessException, InstantiationException{
		
		if (list == null) {
			throw new RuntimeException("list can not be null!");
		}
		Container container = null ;
		if (c == null) {
			container = this ;
		}
		else {
			container = c;
		}
		
		container._c = list.getClass() ;
		container._v = new HashMap() ;
		container._vt = new HashMap() ;
		
		for (int k = 0 ; k < list.size() ; k++) {
			Object v = list.get(k) ;
			if (v instanceof Map) {
				_v.put(k, serializableMap((Map) v , new Container()) ); 
				_vt.put(k, v.getClass()) ;
			}
			if (v instanceof List) {
				_v.put(k, serializableList((List) v , new Container()) ); 
				_vt.put(k, v.getClass()) ;
			}
			else if (v instanceof Object && !(v instanceof String) && !(v instanceof Date)) {
				_v.put(k, serializableObj(v,new Container())); 
				_vt.put(k, v.getClass()) ;
			}
			else {//把基础类型放到_valuemap
				_v.put(k,v);
				_vt.put(k, v.getClass()) ;
			}
		}
		return container;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object serializableObj(Object obj , Container c) throws JsonProcessingException, IllegalArgumentException, IllegalAccessException, InstantiationException{
		if (obj == null) {
			return null ;
		}
		Container container = null ;
		if (c == null) {
			container = this ;
		}
		else {
			container = c;
		}
		container._c = obj.getClass() ;
		container._v = new HashMap() ;
		container._vt = new HashMap() ;
		Field[] fds = obj.getClass().getFields() ;
		
		for (Field f : fds) {
			
			container._vt.put(f.getName(), f.getType());
			
			if (isBasicType(f.getType())) {
				container._v.put(f.getName(), f.get(obj));
			}
			else {
				if (f.getType() == Map.class){
					container._v.put(f.getName(), serializableMap((Map) f.get(obj),new Container()));
				}
				else if ((f.getType().newInstance() instanceof Object)) {
					container._v.put(f.getName(), serializableObj(f.get(obj),new Container()));
				}
			}
		}
		
		return container;
	}
	
	public static boolean isBasicType(Class clazz) {
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

}
