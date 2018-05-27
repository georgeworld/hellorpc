package com.github.hellorpc.configuration;

import com.github.hellorpc.annotation.ClientRootConfiguration;
import com.github.hellorpc.client.ClientInitializer;
import com.github.hellorpc.nio.client.ClientInitializerNettyImpl;

/**
 * @author George (GeorgeWorld@qq.com)
 */
@ClientRootConfiguration
public class ClientConfigurationDefaultImpl implements ClientConfiguration {
    @Override
    public ClientInitializer getClientInitializer() {
        return new ClientInitializerNettyImpl();
    }
}
