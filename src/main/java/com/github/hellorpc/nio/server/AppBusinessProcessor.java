package com.github.hellorpc.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * ҵ���߼�����������,����˸��ͻ��˷ֱ�ʵ���Լ��Ĵ����߼�
 * 
 * @author myumen
 * @date 2017.09.26
 *
 */
public abstract class AppBusinessProcessor {
	protected static Logger logger = LoggerFactory.getLogger(AppBusinessProcessor.class);

	/**
	 * ִ��ҵ���������ǰ�������������Ϣ��������Ϻ󣬽���Ӧ��Ϣ���õ�message������
	 * 
	 * @param message
	 *            ����/��Ӧ��Ϣ����
	 */
	public abstract void process(NettyMessage message);
}
