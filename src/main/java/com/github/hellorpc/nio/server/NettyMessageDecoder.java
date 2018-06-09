package com.github.hellorpc.nio.server;

import java.util.List;

import com.github.hellorpc.nio.server.NettyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <p>
 * ���룬���ֽ�����ת��ΪNettyMessage����
 * 
 * @author myumen
 * @date 2017.09.27
 */
public class NettyMessageDecoder extends ByteToMessageDecoder {

	private static Logger logger = LoggerFactory.getLogger(NettyMessageDecoder.class);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// ��������ͷ����,����
		if (in.readableBytes() < NettyMessage.HEAD_LEN) {
			return;
		}
		
		in.markReaderIndex();
		int magicNumber = in.readInt();
		int length = in.readInt();
		int messageType = in.readInt();
		int logId = in.readInt();
		byte flag = in.readByte();
		
		// ���magicNumber�Բ��ϻ���lengthΪ���������п�����ͨ��telnet������������ݣ�ֱ�Ӷ�����������Ҫ����readerIndex
		if (magicNumber != Constants.MAGIC_NUMBER || length < 0) {
			logger.warn("�Ƿ�����,����");
			return;
		}
		
		// ���ȳ�����Ϣͷ����,����ʣ�µĲ���һ�������ı���,��ô������readerIndex�����صȶ�ȡ����������ٴ���
		if (in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}
		
		NettyMessage message = new NettyMessage(magicNumber, length, messageType, logId);
		message.setFlag(flag);
		byte[] bodyArray = new byte[length];
		in.readBytes(bodyArray);
		message.setMessageBody(bodyArray);
		
		out.add(message);
	}
}
