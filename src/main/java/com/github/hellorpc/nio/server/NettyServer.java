package com.github.hellorpc.nio.server;

import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import com.github.hellorpc.nio.handler.ServerHeartbeatHandler;
import com.github.hellorpc.nio.proto.PacketProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * @author George (GeorgeWorld@qq.com)
 */
public class NettyServer {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(NettyServer.class);
    private static final int DEFAULT_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;
    private int port;
    private ServerBootstrap bootstrap;
    private EventLoopGroup acceptorGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup(DEFAULT_THREAD_NUM);
    private Channel ch;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(acceptorGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast(new ProtobufDecoder(PacketProto.Packet.getDefaultInstance()));
                            ch.pipeline().addLast(new IdleStateHandler(6, 0, 0));
                            ch.pipeline().addLast(new ServerHeartbeatHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)//TCP缓冲区大小
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024) // 这是接收缓冲大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true);//保持长连接

            ch = bootstrap.bind(port).sync().channel();
            LOG.info("server started sucessfully.");
            ch.closeFuture().sync();
        } catch (Throwable t) {
            LOG.error("异常", t);
        } finally {
            acceptorGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void close() {
        if (ch != null) {
            ch.close();
        }

        if (bootstrap != null) {
            if (bootstrap.config().group() != null) {
                bootstrap.config().group().shutdownGracefully();
            }
            if (bootstrap.config().childGroup() != null) {
                bootstrap.config().childGroup().shutdownGracefully();
            }
        }
    }

    public int getPort() {
        return port;
    }
}
