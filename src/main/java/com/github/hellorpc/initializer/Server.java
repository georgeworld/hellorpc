package com.github.hellorpc.initializer;

import com.github.hellorpc.configuration.ServerRPConfiguration;
import com.github.hellorpc.container.Ioc;
import com.github.hellorpc.def.HelloConstants;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import com.github.hellorpc.nio.server.NettyServer;

import java.io.IOException;

/**
 * Hello Server 启动器
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class Server {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(Server.class);
    private Hello hello;

    public Server(Hello hello) {
        this.hello = hello;
    }

    public Hello getHello() {
        return hello;
    }

    private static void waitToQuit(NettyServer server) throws IOException {
        LOG.info("Server is running on port {}, press q to quit." + server.getPort());
        boolean input = true;
        while (input) {
            int b = System.in.read();
            switch (b) {
                case 'q':
                    LOG.info("Server关闭...");
                    server.close();
                    input = false;
                    break;
                case '\r':
                case '\n':
                    break;
                default:
                    LOG.info("q -- quit.");
                    break;
            }
        }
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

        try {
            waitToQuit(nettyServer);
        } catch (IOException e) {
            LOG.error(e);
            //e.printStackTrace();
        }

        return hello;
    }
}
