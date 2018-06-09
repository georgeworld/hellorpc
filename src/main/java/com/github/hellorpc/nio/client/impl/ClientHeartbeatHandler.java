package com.github.hellorpc.nio.client.impl;

import com.georgeinfo.ginkgo.injection.bean.BeanScope;
import com.github.hellorpc.client.ClientInitializer;
import com.github.hellorpc.container.Ioc;
import com.github.hellorpc.def.HelloConstants;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleStateEvent;
import com.github.hellorpc.nio.proto.PacketProto.Packet;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.github.hellorpc.nio.proto.PacketProto.Packet.newBuilder;

/**
 * 客户端心跳处理器
 * @author George (GeorgeWorld@qq.com)
 */
public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(ClientHeartbeatHandler.class);
    private ClientInitializer client;

    public ClientHeartbeatHandler(ClientInitializer client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--- Server is active ---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--- Server is inactive ---");

        // 10s 之后尝试重新连接服务器
        System.out.println("10s 之后尝试重新连接服务器...");
        Thread.sleep(10 * 1000);

        //获取Channel ID映射关系Map
        ConcurrentHashMap<String, InetSocketAddress> chIdAddressMap = Ioc.getBeanById(HelloConstants.CHANNEL_ID_ADDRESS_KEY);

        if (chIdAddressMap != null && !chIdAddressMap.isEmpty()) {
            InetSocketAddress address = chIdAddressMap.get(ctx.channel().id().asLongText());
            if (address != null) {
                //尝试重新连接Channel

                final EventLoop eventLoop = ctx.channel().eventLoop();
                eventLoop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.doInit(new Bootstrap(), eventLoop);
                        } catch (Exception e) {
                            LOG.error("### Exception when re-connect to server in [" + ClientHeartbeatHandler.class.getName() + "]", e);
                            return;
                        }
                    }
                }, 1L, TimeUnit.SECONDS);
                super.channelInactive(ctx);
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 不管是读事件空闲还是写事件空闲都向服务器发送心跳包
            sendHeartbeatPacket(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("连接出现异常");
    }

    /**
     * 发送心跳包
     *
     * @param ctx
     */
    private void sendHeartbeatPacket(ChannelHandlerContext ctx) {
        Packet.Builder builder = newBuilder();
        builder.setPacketType(Packet.PacketType.HEARTBEAT);
        Packet packet = builder.build();
        ctx.writeAndFlush(packet);
    }
}
