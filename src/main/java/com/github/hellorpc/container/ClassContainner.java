package com.github.hellorpc.container;

import java.lang.reflect.Field;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClassContainner {
	
	private Class clz ;
	private Object obj ;
	
	public ClassContainner(){
		
	}
	
	public ClassContainner(Object obj) throws IllegalArgumentException, IllegalAccessException{
		this.clz = obj.getClass() ;
		this.obj = obj ;
		Field fds[] = this.clz.getDeclaredFields() ;
		for (Field f : fds) {
			Object valobj = null ;
			if (f.getType() == Map.class) {
				valobj = MapContainer.serializable2Obj((Map)obj) ;
				f.set(obj, valobj);
			}
			else if (f.getType() instanceof Object) {
				valobj = serializable2Obj(obj) ;
				f.set(obj, valobj);
			}
			else {
				f.set(obj, valobj);
			}
		}
		try {
			new ObjectMapper().writeValueAsBytes(this) ;
		} catch (JsonProcessingException e) {
			System.err.println(e);
		}
		
	}
	
	public Class getClz() {
		return clz;
	}
	public void setClz(Class clz) {
		this.clz = clz;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	private static Object serializable2Obj(Object v){
		ClassContainner mc = null ;
		try {
			
			mc = new ClassContainner(v) ;
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		
		
		return mc ;
	}

}
