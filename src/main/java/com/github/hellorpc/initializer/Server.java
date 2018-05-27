package com.github.hellorpc.initializer;

import com.github.hellorpc.configuration.ServerRPConfiguration;
import com.github.hellorpc.container.Ioc;
import com.github.hellorpc.def.HelloConstants;
import com.github.hellorpc.nio.server.NettyServer;

/**
 * Hello Server 启动器
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class Server {
    private Hello hello;

    public Server(Hello hello) {
        this.hello = hello;
    }

    public Hello getHello() {
        return hello;
    }

    /**
     * 启动服务器
     */
    public Hello startServer() throws Exception {
        //从DI容器中，取出RPC配置类实例
        ServerRPConfiguration config = Ioc.getBeanById(HelloConstants.SERVER_CONFIG_OBJECT_KEY);
        int port = config.getPort();

        //启动Netty 服务端监听线程池
        NettyServer nettyServer = new NettyServer(port);
        nettyServer.run();

        return hello;
    }
}
