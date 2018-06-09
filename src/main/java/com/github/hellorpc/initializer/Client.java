package com.github.hellorpc.initializer;

import com.georgeinfo.ginkgo.injection.context.ApplicationContext;
import com.github.hellorpc.client.ClientInitializer;
import com.github.hellorpc.configuration.ClientConfiguration;
import com.github.hellorpc.container.Ioc;
import com.github.hellorpc.def.HelloConstants;

/**
 * 客户端初始化入口
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class Client {
    private Hello hello;
    private ApplicationContext context;

    public Client(ApplicationContext context, Hello hello) {
        this.context = context;
        this.hello = hello;
    }

    public Hello getHello() {
        return hello;
    }

    /**
     * 初始化客户端环境
     */
    public Hello startClient() throws Exception {
        //从DI容器中，取出RPC客户端配置类实例
        ClientConfiguration config = Ioc.getBeanById(HelloConstants.CLIENT_CONFIG_OBJECT_KEY);

        //执行客户端初始化
        ClientInitializer client = config.getClientInitializer(context);
        client.doInit();
        return hello;
    }
}
