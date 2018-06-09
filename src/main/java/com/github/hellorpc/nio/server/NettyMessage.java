package com.github.hellorpc.nio.server;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;


/**
 * <p>
 * ��Ϣ���󣬸���Э�鶨����Ϣ��ʽ����Ϣ��ʽΪ��magicNumber+length+messageType+logId+flag+��Ϣ��byte[]��
 * ǰ16���ֽڷֱ��Ӧ4�������ֶΣ�����˴�ȡ����,��17���ֽ�ͨ��0|1��ʾ����|��Ӧ��
 * 
 * @author myumen
 * @date 2017.09.26
 */
public class NettyMessage implements Serializable {
	private static final long serialVersionUID = 3201210212398130551L;

	/** ��Ϣͷ�ֽڳ��� */
	public static final int HEAD_LEN = 17;

	/** ҵ���� */
	public static final int MESSAGE_TYPE_BIZ = 1;

	/** �������� */
	public static final int MESSAGE_TYPE_HB = 2;

	/** ħ������Լ��һ���ض����֣������Դ�ֵ��ͷ�ı��Ĳ�����Ч����  */
	private int magicNumber = Constants.MAGIC_NUMBER;

	/** ��Ϣ�峤�ȣ���messageBody.length */
	private int length = 0;

	/** ��Ϣ���ͣ�1��ҵ����Ϣ��2��������Ϣ */
	private int messageType = MESSAGE_TYPE_BIZ;

	/** �����������logId����Ӧ������ֵԭ·�������󷽣����Ա�ʶͬһ����Ϣ */
	private int logId = 0;

	/** ��ʾ������0������Ӧ1 */
	private byte flag = 0;

	/** ��Ϣͷ��16�ֽڳ��ȣ��������ĸ�������ɣ�magicNumber|length|messageType|logId�����ְ���˴�ȡ */
	private byte[] messageHead;

	/** ��Ϣ�壬Ĭ��ΪUTF-8���� */
	private byte[] messageBody;

	/** Ĭ���������� */
	public static final NettyMessage HEATBEAT_MSG = buildHeartBeatMsg();

	public static NettyMessage buildHeartBeatMsg() {
		NettyMessage hb = new NettyMessage();
		hb.setMessageType(MESSAGE_TYPE_HB);
		hb.setLogId(10000000); // ��������logIdĬ������Ϊ10000000
		hb.setMessageBody("HB".getBytes()); // Ĭ�ϱ��뼴��,Ӣ���ַ������б���������һ����
		return hb;
	}

	public NettyMessage() {
	}

	public NettyMessage(int magicNumber, int length, int messageType, int logId) {
		super();
		this.magicNumber = magicNumber;
		this.length = length;
		this.messageType = messageType;
		this.logId = logId;
	}

	public NettyMessage(String msg) {
		if (msg == null || msg.length() == 0) {
			return;
		}
		try {
			this.messageBody = msg.getBytes(Constants.ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// ���֧�����õı����ʽ�������ϵͳĬ�ϵı���
			this.messageBody = msg.getBytes();
		}
		this.length = this.messageBody.length;
	}

	public NettyMessage(byte[] fullMsg) {
		if (fullMsg == null || fullMsg.length < HEAD_LEN) {
			return;
		}

		this.messageHead = new byte[HEAD_LEN];
		System.arraycopy(fullMsg, 0, messageHead, 0, HEAD_LEN);
		this.parseHead();
		if (fullMsg.length > HEAD_LEN) {
			this.messageBody = new byte[this.length];
			System.arraycopy(fullMsg, HEAD_LEN, this.messageBody, 0, this.length);
		}
	}

	public int getMagicNumber() {
		return magicNumber;
	}

	public void setMagicNumber(int magicNumber) {
		this.magicNumber = magicNumber;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public byte[] getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
		if (this.messageBody != null) {
			this.length = this.messageBody.length;
		}
	}

	public void setMessageBody(String mb) {
		if (mb == null || mb.length() == 0) {
			return;
		}
		try {
			this.messageBody = mb.getBytes(Constants.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.messageBody = mb.getBytes();
		}
		this.length = this.messageBody.length;
	}

	public byte[] getMessageHead() {
		if (this.messageHead == null) {
			this.composeHead();
		}
		return messageHead;
	}

	public void setMessageHead(byte[] messageHead) {
		this.messageHead = messageHead;
		this.parseHead();
	}

	private void parseHead() {
		if (messageHead == null || messageHead.length != HEAD_LEN) {
			return;
		}
		byte[] tmps = new byte[4];
		System.arraycopy(messageHead, 0, tmps, 0, 4);
		this.magicNumber = ByteTransUtil.byteArrayToInt(tmps, false);

		System.arraycopy(messageHead, 4, tmps, 0, 4);
		this.length = ByteTransUtil.byteArrayToInt(tmps, false);

		System.arraycopy(messageHead, 8, tmps, 0, 4);
		this.messageType = ByteTransUtil.byteArrayToInt(tmps, false);

		System.arraycopy(messageHead, 12, tmps, 0, 4);
		this.logId = ByteTransUtil.byteArrayToInt(tmps, false);
	}

	private void composeHead() {
		this.messageHead = new byte[HEAD_LEN];
		System.arraycopy(ByteTransUtil.intToByteArray(this.magicNumber, false), 0, messageHead, 0, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.length, false), 0, messageHead, 4, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.messageType, false), 0, messageHead, 8, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.logId, false), 0, messageHead, 12, 4);
		this.messageHead[HEAD_LEN - 1] = flag;
	}

	@Override
	public String toString() {
		return MessageFormat.format(
				"Msg[magicNumber={0,number,###},length={1,number,###},messageType={2,number,###},logId={3,number,###},flag={4,number,###}][{5}]",
				new Object[] { magicNumber, length, messageType, logId, flag, bodyToString() });
	}

	public String bodyToString() {
		String body = null;
		if (this.messageBody != null && this.messageBody.length > 0) {
			try {
				body = new String(messageBody, Constants.ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				body = new String(messageBody);
			}
		}
		return body;
	}

	/**
	 * ����������Ϣ��Ӧ���ֽ����飬���û����Ϣ�壬��ֻ��ͷ��
	 * 
	 * @return
	 */
	public byte[] composeFull() {
		if (this.messageBody != null) {
			this.length = this.messageBody.length;
		}

		byte[] data = new byte[this.length + HEAD_LEN];
		System.arraycopy(ByteTransUtil.intToByteArray(this.magicNumber, false), 0, data, 0, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.length, false), 0, data, 4, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.messageType, false), 0, data, 8, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.logId, false), 0, data, 12, 4);
		data[HEAD_LEN - 1] = flag;
		if (this.messageBody != null) {
			System.arraycopy(this.messageBody, 0, data, HEAD_LEN, this.length);
		}
		return data;
	}
}
