package com.github.hellorpc.nio.client;

import com.georgeinfo.ginkgo.injection.bean.BeanScope;
import com.georgeinfo.ginkgo.injection.context.ApplicationContext;
import com.github.hellorpc.client.ClientInitializer;
import com.github.hellorpc.def.HelloConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Hello RPC客户端初始化器Netty实现类
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class ClientInitializerNettyImpl implements ClientInitializer {
    final EventLoopGroup group = new NioEventLoopGroup();
    final Bootstrap strap = new Bootstrap();
    protected final HashedWheelTimer timer = new HashedWheelTimer();
    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();
//    InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8080);
//    InetSocketAddress addr2 = new InetSocketAddress("10.0.0.11", 8888);

    ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    public ChannelPoolMap<InetSocketAddress, SimpleChannelPool> getPoolMap() {
        return poolMap;
    }

    public void doInit(ApplicationContext context) throws Exception {
        final ConnectionWatchdog watchdog = new ConnectionWatchdog(strap, timer, 9199,"127.0.0.1", true) {

            public ChannelHandler[] handlers() {
                return new ChannelHandler[] {
                        this,
                        new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                        idleStateTrigger,
                        new StringDecoder(),
                        new StringEncoder(),
                        new HeartBeatClientHandler()
                };
            }
        };

        strap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(), 2);
            }
        };

        //将连接池放入DI容器
        context.addBean(HelloConstants.CLIENT_POOL_KEY, poolMap, BeanScope.singleton);
    }
}
