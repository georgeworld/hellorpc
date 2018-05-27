package com.github.hellorpc.client;

/**
 * 客户端调用类
 *
 * @author George (GeorgeWorld@qq.com)
 */
public interface HelloRPC {
    /**
     * 根据服务接口获得服务实现类对象
     */
    public <T> T getService(Class<T> serviceApi);
}
