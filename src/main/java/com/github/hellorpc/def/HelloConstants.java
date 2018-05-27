package com.github.hellorpc.def;

/**
 * 全局常量定义类
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class HelloConstants {
    /**
     * ServerRPConfiguration 类实例在DI容器中的key
     */
    public static final String SERVER_CONFIG_OBJECT_KEY = "_RPConfigImpl";
    /**
     * 客户端 ClientConfiguration 类实例在DI容器中的key
     */
    public static final String CLIENT_CONFIG_OBJECT_KEY = "_RPClientConfigImpl";

    /**
     * 客户端连接池对象，在DI容器中的key
     */
    public static final String CLIENT_POOL_KEY = "_RPClientPool";
}
