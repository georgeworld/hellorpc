package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.MsgContent;
import com.github.hellorpc.serialize.HelloRPCSerialize.ObjectType;
import com.github.hellorpc.tlv.bertlv.TlvBuilder;
import com.github.hellorpc.tlv.bertlv.TlvStruct;
import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.ByteArrayTool;
import com.github.hellorpc.util.SerializeTool;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George <Georgeinfo@163.com>
 */
public class MsgContentMarshaler {

    private final MsgBuilder msgBuilder;
    private final MsgContent msgContent;

    public MsgContentMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
        msgContent = new MsgContent();
    }

    public MsgContentMarshaler addPackagePath(String packagePath) {
        msgContent.setPackagePath(packagePath);
        return this;
    }

    public MsgContentMarshaler addActionClassName(String actionClassName) {
        msgContent.setActionClassName(actionClassName);
        return this;
    }

    public MsgContentMarshaler addReturnValue(Object returnValue) {
        msgContent.setReturnValue(returnValue);
        return this;
    }

    public MsgContentMarshaler addMethodName(String methodName) {
        msgContent.setMethodName(methodName);
        return this;
    }

    public MsgContentMarshaler addMethodParamsList(ArrayList<Object> methodParamsList) {
        msgContent.setMethodParamsList(methodParamsList);
        return this;
    }
    
    public MsgContentMarshaler addMethodParamsTypeList(ArrayList<Class> methodParamsTypeList) {
//    public MsgContentMarshaler addMethodParamsTypeList(ArrayList<String> methodParamsTypeList) {
        msgContent.setMethodParamsTypeList(methodParamsTypeList);
        return this;
    }

    public MsgBuilder done() throws IOException {
        msgBuilder.getMsg().setMsgContent(msgContent);
        msgBuilder.getDataMap().put(MsgType.MsgContent, pack());
        return msgBuilder;
    }
    
    public byte[] pack() throws IOException{
        byte[] packageData = null;
        {
            String packagePath = msgContent.getPackagePath();
            if(packagePath!=null){
                packageData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("package").setValue(SerializeTool.obj2byte(packagePath)).end();
            }
        }
        byte[] actionClassNameData = null;
        {
            String actionClassName = msgContent.getActionClassName();
            if(actionClassName!=null){
                actionClassNameData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("actionClassName").setValue(SerializeTool.obj2byte(actionClassName)).end();
            }
        }
        byte[] returnTypeData = null;   //返回值类型
        byte[] returnValueData = null;   //返回值数据
        {
            Object rv = msgContent.getReturnValue();
            if(rv!=null){
                ObjectType type = ObjectType.getByClass(rv.getClass());
                returnTypeData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("returnType").setValue(new byte[]{type.getCode()}).end();
                returnValueData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("returnValue").setValue(SerializeTool.obj2byte(rv)).end();
            }else{
                returnTypeData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("returnType").setValue(new byte[]{ObjectType.VOID.getCode()}).end();
            }
        }
        byte[] methodNameData = null;
        {
            String methodName = msgContent.getMethodName();
            if(methodName!=null){
                methodNameData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("methodName").setValue(SerializeTool.obj2byte(methodName)).end();
                byte[] bs = SerializeTool.obj2byte(methodName) ;
            }
        }
        byte[] methodParamList = null;
        {
            List<Object> params = msgContent.getMethodParamsList();
            List<Class> paramsType = msgContent.getMethodParamsTypeList();
//            List<String> paramsType = msgContent.getMethodParamsTypeList();
            if(params!=null){
                byte[] paramsData = null;
                for(int i=0;i<params.size();i++){
                    Object param = params.get(i);
                    Class type = paramsType.get(i);
//                    String type = paramsType.get(i);
                    byte[] paramTypeData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("paramType").setValue(new byte[]{ObjectType.getByClass(type).getCode()}).end();
                    byte[] paramValueData = TlvBuilder.begin().setConstructedDataFlag(false).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("paramValue").setValue(SerializeTool.obj2byte(param)).end();
                    byte[] paramData = TlvBuilder.begin().setConstructedDataFlag(true).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("param").setValue(ByteArrayTool.concat(paramTypeData,paramValueData)).end();
                    
                    
                    paramsData = ByteArrayTool.concat(paramsData,paramData);
                }
                if(paramsData!=null){
                    methodParamList = TlvBuilder.begin().setConstructedDataFlag(true).setFrameType(TlvStruct.FrameType.private_frame).setPureBerTag("params").setValue(paramsData).end();
                }
            }
        }
        //包路径      动作类名             返回值类型      返回值内容       动作方法名称   参数列表
        byte b[] = ByteArrayTool.concat(packageData,actionClassNameData,returnTypeData,returnValueData,methodNameData,methodParamList);
        return b;
    }
    
}
