package com.github.hellorpc.nio.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.hellorpc.nio.server.NettyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * <p>
 * �ͻ��˹����࣬���ڽ���UI��NettyClient������������Ӧ�Ķ���ʵ����Э���������ݣ����ṩ���ӡ��Ͽ���������Ϣ����ڷ�����
 * 
 * @author myumen
 * @date 2017.09.27
 */
public final class NettyClientHelper {
	private static Logger logger = LoggerFactory.getLogger(NettyClientHelper.class);
	
	private static final List<NettyClientHelper> instances = new ArrayList<>();

	private volatile NettyClient client;

	private volatile UIUpdater updater;

	public NettyClientHelper() {
		instances.add(this);
	}

	public NettyClient getClient() {
		return client;
	}

	public void setClient(NettyClient client) {
		this.client = client;
	}

	public UIUpdater getUpdater() {
		return updater;
	}

	public void setUpdater(UIUpdater updater) {
		this.updater = updater;
	}

	public static NettyClientHelper getFirst() {
		return instances.size() > 0 ? instances.get(0) : null;
	}

	public void setRemoteHost(String ip, int port) {
		client.setIp(ip);
		client.setPort(port);
	}

	public void connect() {
		client.connect();
		// connectOrDisconnectΪ���������UIUpdater��ע�������
		updater.update("connectOrDisconnect", new Object[] { "�Ͽ�" });
	}

	public void disConnect() {
		client.disConnect();
		updater.update("connectOrDisconnect", new Object[] { "����" });
	}

	public void stopAll() {
		for (NettyClientHelper helper : instances) {
			if (helper.getClient() != null) {
				helper.getClient().close();
			}
		}
	}

	public void sendMessage(String message) {
		NettyMessage bizMsg = new NettyMessage(message);
		bizMsg.setLogId(newLogId());
		logger.info("������Ϣ  -- {}", bizMsg.toString());
		ByteBuf buf = Unpooled.copiedBuffer(bizMsg.composeFull());
		client.future.channel().writeAndFlush(buf);
		// ReferenceCountUtil.release(buf);
	}

	/**
	 * �������logId
	 * 
	 * @return logId ����[1000000,10000000]���������
	 */
	private int newLogId() {
		int logId = randomInt(1000000, 10000000);
		if (logId % 2 == 0) {
			logId -= 1;
		}

		return logId;
	}

	/**
	 * ���ɽ���min��max֮����������
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random random = new Random(System.currentTimeMillis());
		// int x = (int) (Math.random() * max + min);
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
}
