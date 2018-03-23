
package com.github.hellorpc.protocol.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class MsgContent {

    /**
     * 包路径(或命名空间路径)
     */
    private String packagePath;
    /**
     * 动作类名称
     */
    private String actionClassName;
    /**
     * 动作方法返回值
     */
    private Object returnValue;
    /**
     * 动作方法名称
     */
    private String methodName;
    /**
     * 动作方法参数列表
     */
    private ArrayList<Object> methodParamsList;
    /**
     * 动作方法参数列表类型
     */
    private ArrayList<Class> methodParamsTypeList;
//    private ArrayList<String> methodParamsTypeList;

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getActionClassName() {
        return actionClassName;
    }

    public void setActionClassName(String actionClassName) {
        this.actionClassName = actionClassName;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ArrayList<Object> getMethodParamsList() {
        return methodParamsList;
    }

    public void setMethodParamsList(ArrayList<Object> methodParamsList) {
        this.methodParamsList = methodParamsList;
    }

    public ArrayList<Class> getMethodParamsTypeList() {
//    public ArrayList<String> getMethodParamsTypeList() {
        return methodParamsTypeList;
    }

    public void setMethodParamsTypeList(ArrayList<Class> methodParamsTypeList) {
//    public void setMethodParamsTypeList(ArrayList<String> methodParamsTypeList) {
        this.methodParamsTypeList = methodParamsTypeList;
    }
    @Override
    public String toString() {
        return "MsgContent{" + "packagePath=" + packagePath + ", actionClassName=" + actionClassName + ", returnValue=" + returnValue + ", methodName=" + methodName + ", methodParamsList=" + methodParamsList + '}';
    }
    
    public Object[] calc(int[] is , double[] ds){
        
        return new Object[2];
    }
    
    public static class User{

        public User() {
        }
        
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public String name ;
        public int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
        

        @Override
        public String toString() {
            return "User{" + "name=" + name + ", age=" + age + '}';
        }
        
    }
    
    public static void main(String[] args) throws Exception{
//        
//        Class clazz =User.class;
//       Object inst =  clazz.newInstance();
//       Object[] objs = new Object[]{inst};
//        
//        Class clazz2 = User[].class;
//        System.out.println(objs.getClass());
//        System.out.println(clazz2);
//        System.out.println(objs.getClass() == clazz2);
//        
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue("",new TypeReference<List<User>>(){});
////        User[] us = new User[]{new User("张三",10),new User("李四",8)};
//        List<User> us = new ArrayList<>();
////        { {
////            add(new User("张三",10));
////            
////            add(new User("李四",8));
////        }}
//        us.add(new User("张三",10));
//        us.add(new User("李四",8));
//        String json = mapper.writeValueAsString(us);
//        us = null;
//        System.out.println(json);
////        us = mapper.readValue(json,new TypeReference<User>() {});
////        us = mapper.readValue(json,List.class);
//
//        List lu = mapper.readValue(json,List.class); 
//        System.out.println(lu.get(0).getClass());
//        us = lu;
        System.out.println(User[].class.getName());
        Class clazz = User.class;
        Class clazz2 = Class.forName("[L"+clazz.getName()+";");
        System.out.println(User[].class == clazz2);
//        us = lu;
//        Object obj = mapper.readValue(json,List.class);
//        System.out.println(obj);
    }
}
