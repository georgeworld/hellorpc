package com.github.hellorpc.nio.client;

import com.github.hellorpc.container.Ioc;
import com.github.hellorpc.def.HelloConstants;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import com.github.hellorpc.nio.proto.PacketProto;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Random;

import static com.github.hellorpc.nio.proto.PacketProto.Packet.newBuilder;

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
                connectAndWrite("这是客户端发送的业务数据");

                return "OK";
            }
        });

        return (T) obj;
    }

    public void connectAndWrite(String messageContent) {
        //从客户端连接池中，取出一个连接

        //从连接池中获取一个NIO socket 连接对象
        ChannelPoolMap<InetSocketAddress, SimpleChannelPool> channelPool = Ioc.getBeanById(HelloConstants.CLIENT_POOL_KEY);
        final SimpleChannelPool pool = channelPool.get(getAddress());
        Future<Channel> f = pool.acquire();
        f.addListener((FutureListener<Channel>) f1 -> {
            if (f1.isSuccess()) {
                Channel ch = f1.getNow();

                //开始发送数据
                Random random = new Random();
                int num = random.nextInt(21);

                PacketProto.Packet.Builder builder = newBuilder();
                builder.setPacketType(PacketProto.Packet.PacketType.DATA);
                builder.setData(messageContent + num);
                PacketProto.Packet packet = builder.build();
                ch.writeAndFlush(packet);
                //结束发送数据

                // Release back to pool
                pool.release(ch);
            }
        });
    }

}
