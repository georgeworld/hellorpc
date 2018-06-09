package com.github.hellorpc.nio.client;

import com.github.hellorpc.nio.server.AppBusinessProcessor;
import com.github.hellorpc.nio.server.NettyMessage;

/**
 * <p>
 * �ͻ������ҵ���߼�����
 * 
 * @author myumen
 * @date 2017.09.27
 *
 */
public class ClientBusinessProcessor extends AppBusinessProcessor {

	public void process(NettyMessage message) {
		NettyClientHelper helper = NettyClientHelper.getFirst();
		if (helper == null) {
			return;
		}
		logger.info("�ͻ���ִ��ҵ����...");
		
		// TODO: biz goes here
		
		// ���½���
		helper.getUpdater().update("responseMessage", new Object[] { message.bodyToString() });
		helper.getUpdater().update("statusMsg", new Object[] { "���յ���Ӧ..." });
	}

}
