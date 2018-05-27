package com.github.hellorpc.nio.server;

import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 心跳检测处理类
 *
 * @author George (GeorgeWorld@qq.com)
 */

public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(HeartBeatServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //收到客户端发来的心跳后，什么都不做，也不用做回应，只是在调试状态下打印一下日志即可。

        LOG.debug("### Netty Server channel received heart beat data:"
                + msg.toString() + " from client:" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("### Exception when heart beat processing.", cause);
        ctx.close();
    }

}