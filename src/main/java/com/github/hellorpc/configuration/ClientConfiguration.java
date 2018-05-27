package com.github.hellorpc.configuration;

import com.github.hellorpc.client.ClientInitializer;

/**
 * 客户端配置类接口
 *
 * @author George (GeorgeWorld@qq.com)
 */
public interface ClientConfiguration {
    public ClientInitializer getClientInitializer();
}
