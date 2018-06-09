package com.github.hellorpc.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * <p>
 * ����ҵ�����Handler��������ҵ���߳���<code>DefaultEventExecutorGroup</code>�У���������̫��ʱ������NIO�߳�������
 * 
 * @author myumen
 * @date 2017.09.26
 *
 */
public class NettyBusinessDuplexHandler extends ChannelDuplexHandler {
	private static Logger logger = LoggerFactory.getLogger(NettyBusinessDuplexHandler.class);

	private AppBusinessProcessor bizProcessor = null;

	public NettyBusinessDuplexHandler(AppBusinessProcessor appBizHandler) {
		super();
		this.bizProcessor = appBizHandler;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage bizMsg = (NettyMessage) msg; // ��ֺõ���Ϣ

		if (bizMsg.getMessageType() == NettyMessage.MESSAGE_TYPE_HB) {
			logger.info("�յ�����  -- {}", bizMsg.toString());
		} else {
			// ����ҵ����Ϣ
			logger.info("�յ���Ϣ  -- {}", bizMsg.toString());
			bizProcessor.process(bizMsg);
			// ������յ�������������Ҫд����Ӧ��Ϣ
			if (bizMsg.getFlag() == 0) {
				bizMsg.setFlag((byte) 1);
				logger.info("д����Ϣ  -- {}", bizMsg.toString());
				ByteBuf rspMsg = Unpooled.copiedBuffer(bizMsg.composeFull());
				ctx.writeAndFlush(rspMsg);
			}
		}
		// �������ݸ�Pipeline��һ��Handler
		// super.channelRead(ctx, msg);
		// ctx.fireChannelRead(msg);
	}
}
