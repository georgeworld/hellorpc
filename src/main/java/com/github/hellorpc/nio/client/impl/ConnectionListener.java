package com.github.hellorpc.nio.client.impl;

import com.github.hellorpc.client.ClientInitializer;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import com.github.hellorpc.util.RPCException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * Netty连接监听器，如果第一次连接未成功，则尝试间隔时间重连
 * @author George (GeorgeWorld@qq.com)
 */
public class ConnectionListener implements ChannelFutureListener {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(ConnectionListener.class);
    private ClientInitializer client;
    public ConnectionListener(ClientInitializer client) {
        this.client = client;
    }
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            System.out.println("Reconnect");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        client.doInit(new Bootstrap(), loop);
                    } catch (Exception e) {
                        LOG.error("### Exception when re-connect to server.",e);
                        return;
                    }
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }
}