package com.github.hellorpc.configuration;

import com.github.hellorpc.annotation.ServerRootConfiguration;

/**
 * 默认RPC配置实现类
 * @author George (GeorgeWorld@qq.com)
 */
@ServerRootConfiguration
public class ServerRPConfigurationDefaultImpl implements ServerRPConfiguration {
    @Override
    public int getPort() {
        return 9919;
    }
}
