package com.github.hellorpc.configuration;

import com.georgeinfo.ginkgo.injection.context.ApplicationContext;
import com.github.hellorpc.annotation.ClientRootConfiguration;
import com.github.hellorpc.client.ClientInitializer;
import com.github.hellorpc.nio.client.impl.ClientInitializerNettyImpl;

/**
 * @author George (GeorgeWorld@qq.com)
 */
@ClientRootConfiguration
public class ClientConfigurationDefaultImpl implements ClientConfiguration {
    @Override
    public ClientInitializer getClientInitializer(ApplicationContext context) {
        return new ClientInitializerNettyImpl(context);
    }
}
