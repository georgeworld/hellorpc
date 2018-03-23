package com.github.hellorpc.protocol.entities;

import com.github.hellorpc.serialize.HelloRPCSerialize.ObjectType;

/**
 *
 * @author dev1
 */
public class TypeAndValue {
    
    public ObjectType returnType;
    
    public Object returnValue;

    public ObjectType getReturnType() {
        return returnType;
    }

    public void setReturnType(ObjectType returnType) {
        this.returnType = returnType;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return "TypeAndValue{" + "returnType=" + returnType + ", returnValue=" + returnValue + '}';
    }
}
