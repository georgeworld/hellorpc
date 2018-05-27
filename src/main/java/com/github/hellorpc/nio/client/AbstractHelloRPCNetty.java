package com.github.hellorpc.nio.client;

import com.github.hellorpc.container.Ioc;
import com.github.hellorpc.def.HelloConstants;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * @author George (GeorgeWorld@qq.com)
 */
public abstract class AbstractHelloRPCNetty implements HelloRPCNetty {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(AbstractHelloRPCNetty.class);

    /**
     * 根据服务接口获得服务实现类对象
     */
    public <T> T getService(Class<T> serviceApi) {
        //从DI容器内，获得Service实例
        T service = Ioc.getBeanByInterface(serviceApi);
        if (service == null) {
            LOG.error("### Can't found service instance [" + serviceApi.getName() + "] from DI container.");
            return null;
        }

        //使用动态代理，执行服务方法的代理执行
        Object obj = Proxy.newProxyInstance(serviceApi.getClassLoader(), new Class[]{serviceApi}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                final String ECHO_REQ = "Hello Netty.$_";

                //从连接池中获取一个NIO socket 连接对象
                ChannelPoolMap<InetSocketAddress, SimpleChannelPool> channelPool = Ioc.getBeanById(HelloConstants.CLIENT_POOL_KEY);
                final SimpleChannelPool pool = channelPool.get(getAddress());
                Future<Channel> f = pool.acquire();
                f.addListener((FutureListener<Channel>) f1 -> {
                    if (f1.isSuccess()) {
                        Channel ch = f1.getNow();
                        ch.writeAndFlush(ECHO_REQ);
                        ch.writeAndFlush("fuck you");
                        // Release back to pool
                        pool.release(ch);
                    }
                });

                return "OK";
            }
        });

        return (T) obj;
    }

}
