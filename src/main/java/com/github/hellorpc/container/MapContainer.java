package com.github.hellorpc.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

public class MapContainer<K,V> implements Serializable {
	
	public Map<K,V> _valuemap ;
	public Class _keytype ;
	public Class _valuetype ;
	
	public MapContainer(){
		
	}
	
	public MapContainer(Map<K,V> map) throws RuntimeException{
		
		if (map == null) {
			throw new RuntimeException("map can not be null!");
		}
		
		try {
			_valuemap = map.getClass().newInstance() ;
		} catch (InstantiationException e1) {
			System.err.println(e1);
		} catch (IllegalAccessException e1) {
			System.err.println(e1);
		}
		
		Set<Entry<K,V>> entrys = map.entrySet() ;
		Iterator<Entry<K,V>> it = entrys.iterator();
		
		while (it.hasNext()) {
			Entry<K,V> entry = (Entry<K, V>) it.next() ;
			K k = entry.getKey();
			V v = entry.getValue();
			Class kclass = k.getClass() ;
			Class vclass = v.getClass() ;
			_keytype = kclass ;
			_valuetype = vclass ;
			if (vclass == Map.class) {
				_valuemap.put(k, (V) this.serializable2Obj((Map) v)); 
			}
			else if (vclass instanceof Object) {
				
			}
			else {
				_valuemap.put(k,v);
			}
		}
	}

	public Map<K, V> get_valuemap() {
		return _valuemap;
	}

	public void set_valuemap(Map<K, V> _valuemap) {
		this._valuemap = _valuemap;
	}

	public Class get_keytype() {
		return _keytype;
	}

	public void set_keytype(Class _keytype) {
		this._keytype = _keytype;
	}

	public Class get_valuetype() {
		return _valuetype;
	}

	public void set_valuetype(Class _valuetype) {
		this._valuetype = _valuetype;
	}
	
	public static Object serializable2Obj(Map v){
		MapContainer mc = null ;
		try {
			
			mc = new MapContainer(v) ;
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		
		
		return mc ;
	}
	
	
}
