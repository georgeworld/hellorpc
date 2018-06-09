package com.github.hellorpc.nio.server;

import com.github.hellorpc.nio.server.NettyMessage;

/**
 * <p>
 * �����ҵ���߼�����
 * 
 * @author myumen
 * @date 2017.09.26
 *
 */
public class ServerBusinessProcessor extends AppBusinessProcessor {
	
	public void process(NettyMessage message) {
		logger.info("�����ִ��ҵ����...");
		String req = message.bodyToString();
		// TODO: biz goes here
		String rsp = req + " ---> ������Ӧ";
		message.setMessageBody(rsp);
	}
}
