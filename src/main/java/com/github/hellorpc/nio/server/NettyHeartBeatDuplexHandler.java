package com.github.hellorpc.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * <p>
 * ����Handler,��ʾ����,��������Ping-Pingģʽ,������˸��ͻ��˻��෢��,���շ��յ�ֱ�Ӷ���,������Ӧ.
 * <p>
 * �������������20��δ�����κ����ݣ��������������60��δ�յ��κ����ݣ�����Ϊ�Է���ʱ���ر����ӣ������˫�������á�
 * 
 * @author myumen
 * @date 2017.09.27
 */
public class NettyHeartBeatDuplexHandler extends ChannelDuplexHandler {
	private static Logger logger = LoggerFactory.getLogger(NettyHeartBeatDuplexHandler.class);

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("���ӹر�" + ctx.channel());
		super.channelInactive(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("���ӽ���" + ctx.channel());
		super.channelActive(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleState state = ((IdleStateEvent) evt).state();
			if (state == IdleState.WRITER_IDLE) { // 20s
				// throw new Exception("idle exception");
				logger.info("idle 20s, send heartbeat");
				ByteBuf buf = Unpooled.copiedBuffer(NettyMessage.HEATBEAT_MSG.composeFull());
				ctx.writeAndFlush(buf);
			} else if (state == IdleState.READER_IDLE) { // 60s
				logger.info("����timeout,����ر� " + ctx.channel());
				ctx.close();
			}
			// ע�����
			// ��Ϊ��ʵ�ֵ��߼�������IdleStateEventֻ��NettyHeartBeatHandlerһ��Handler�����ɣ�
			// ���Կ��Բ���Ҫ���¼�������pipeline������Handler���ݣ���Ȼ������Ҳûʲô�£���Ϊ�����ĵط����ܴ���
			// ��ĳЩ����£�����㶨����¼���Ҫ֪ͨ���Handler������ôһ��Ҫ����������һ����С�
			// super.userEventTriggered(ctx, evt);
		} else {
			// �����¼�ת����Pipeline��������Handler����
			super.userEventTriggered(ctx, evt);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("�쳣: ", cause);
		ctx.close();
	}
}
