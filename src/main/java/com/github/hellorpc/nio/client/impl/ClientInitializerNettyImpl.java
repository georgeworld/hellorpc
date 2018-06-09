package com.github.hellorpc.nio.client.impl;

import com.georgeinfo.ginkgo.injection.bean.BeanScope;
import com.georgeinfo.ginkgo.injection.context.ApplicationContext;
import com.github.hellorpc.client.ClientInitializer;
import com.github.hellorpc.def.HelloConstants;
import com.github.hellorpc.nio.client.ConnectorIdleStateTrigger;
import com.github.hellorpc.nio.client.NettyChannelPoolHandler;

import static com.github.hellorpc.nio.proto.PacketProto.Packet.newBuilder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello RPC客户端初始化器Netty实现类
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class ClientInitializerNettyImpl implements ClientInitializer {
    final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private static Bootstrap bootstrapDefault = new Bootstrap();
    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();
    //    InetSocketAddress addr1 = new InetSocketAddress("127.0.0.1", 8080);
//    InetSocketAddress addr2 = new InetSocketAddress("10.0.0.11", 8888);
    private ApplicationContext context;

    ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    public ClientInitializerNettyImpl(ApplicationContext context) {
        this.context = context;
    }

    public ChannelPoolMap<InetSocketAddress, SimpleChannelPool> getPoolMap() {
        return poolMap;
    }

    public Bootstrap getBootstrap() {
        return bootstrapDefault;
    }


    private class HelloChannelInitializer extends ChannelInitializer<SocketChannel> {
        private ClientInitializer clientInitializer;

        public HelloChannelInitializer(ClientInitializer clientInitializer) {
            this.clientInitializer = clientInitializer;
        }

        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            ch.pipeline().addLast(new ProtobufEncoder());
            ch.pipeline().addLast(new IdleStateHandler(0, 5, 0));
            ch.pipeline().addLast(new ClientHeartbeatHandler(clientInitializer));
        }
    }

    public void doInit() throws Exception {
        doInit(bootstrapDefault, eventLoopGroup);
    }
    
    public void doInit(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) throws Exception {
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new HelloChannelInitializer(this));

        //定义Channel ID 与 InetSocketAddress的映射关系
        ConcurrentHashMap<String, InetSocketAddress> chIdAddressMap = new ConcurrentHashMap<String, InetSocketAddress>();

        //初始化客户端连接池
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                FixedChannelPool channel = new FixedChannelPool(bootstrap.remoteAddress(key), new NettyChannelPoolHandler(), 2);


                //创建连接池后，随之将channel从连接池中取出来，提取要素放入映射map 开始
                Future<Channel> f = channel.acquire();
                f.addListener((FutureListener<Channel>) f1 -> {
                    if (f1.isSuccess()) {
                        Channel ch = f1.getNow();

                        chIdAddressMap.put(ch.id().asLongText(), key);
                    }
                });
                //创建连接池后，随之将channel从连接池中取出来，提取要素放入映射map 结束

                return channel;
            }
        };

        //将连接池放入DI容器
        context.addBean(HelloConstants.CLIENT_POOL_KEY, poolMap, BeanScope.singleton);

        //将映射关系map放入DI容器
        context.addBean(HelloConstants.CHANNEL_ID_ADDRESS_KEY, chIdAddressMap, BeanScope.singleton);
    }


    public ApplicationContext getApplicationContext() {
        return this.context;
    }
}
