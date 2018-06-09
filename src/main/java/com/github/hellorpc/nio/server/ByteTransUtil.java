package com.github.hellorpc.nio.server;

import java.nio.ByteBuffer;

/**
 * �ֽڸ�Java�������͵�ת������
 * 
 * @author XUHAILIN730
 * 
 */
public final class ByteTransUtil {
	public static byte[] intToByteArray1(int i) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(i < 0 ? ~i : i)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++) {
			byteArray[3 - n] = (byte) (i >>> (n * 8));
		}
		return byteArray;
	}

	/**
	 * ������ת��Ϊ�ֽ����飬BIG-ENDIAN��ʽ������λ�����ڸߵ�ַ����λ�����ڵ͵�ַ��
	 * 0xff������[00000000][00000000][00000000][11111111]��������0xff������(&)
	 * ���൱��Ĩȥ���ֵĸ�λ3���ֽڣ�ֻ���µ�λ1���ֽ�
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	/**
	 * ������ת��Ϊ�ֽ����飬���ɲ��������Ƿ���С�˻��Ǵ��
	 * 
	 * @param i
	 * @param littleEndian
	 *            true��ʾ��С�˸�ʽ,false��ʾ���
	 * @return
	 */
	public static byte[] intToByteArray(int i, boolean littleEndian) {
		byte[] result = new byte[4];
		if (littleEndian) {
			result[0] = (byte) (i & 0xFF);
			result[1] = (byte) ((i >> 8) & 0xFF);
			result[2] = (byte) ((i >> 16) & 0xFF);
			result[3] = (byte) ((i >> 24) & 0xFF);
		} else {
			result[0] = (byte) ((i >> 24) & 0xFF);
			result[1] = (byte) ((i >> 16) & 0xFF);
			result[2] = (byte) ((i >> 8) & 0xFF);
			result[3] = (byte) (i & 0xFF);
		}
		return result;
	}

	public static byte[] intToByteArray2(int i) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(i);
		return buffer.array();
	}

	/**
	 * byte[]ת��Ϊ������byte[]Ϊ��˸�ʽ
	 * 
	 * @param b
	 * @return
	 */
	public static int byteArrayToInt(byte[] b) {
		int i = 0;
		i += ((b[0] & 0xff) << 24);
		i += ((b[1] & 0xff) << 16);
		i += ((b[2] & 0xff) << 8);
		i += ((b[3] & 0xff));
		return i;
	}

	/**
	 * byte[]ת��Ϊ������ͨ������ȷ��byte[]Ϊ��˸�ʽ����С�˸�ʽ
	 * 
	 * @param b
	 * @param littleEndian
	 *            trueΪС��,falseΪ���
	 * @return
	 */
	public static int byteArrayToInt(byte[] b, boolean littleEndian) {
		int i = 0;
		if (littleEndian) {
			i += ((b[0] & 0xff));
			i += ((b[1] & 0xff) << 8);
			i += ((b[2] & 0xff) << 16);
			i += ((b[3] & 0xff) << 24);
		} else {
			i += ((b[0] & 0xff) << 24);
			i += ((b[1] & 0xff) << 16);
			i += ((b[2] & 0xff) << 8);
			i += ((b[3] & 0xff));
		}
		return i;
	}

	public static int byteArrayToInt2(byte[] b) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put(b);
		buffer.flip();
		return buffer.getInt();
	}

	/**
	 * ����ת�������紫����ֽ������ֽ����飩�����ݣ�С�˸�ʽ
	 * 
	 * @param num
	 *            һ����������
	 * @return 4���ֽڵ��Լ�����
	 */
	public static byte[] intToBytes(int num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & (num >> 0));
		bytes[1] = (byte) (0xff & (num >> 8));
		bytes[2] = (byte) (0xff & (num >> 16));
		bytes[3] = (byte) (0xff & (num >> 24));
		return bytes;
	}

	/**
	 * �ĸ��ֽڵ��ֽ�����(С��)ת����һ����������
	 * 
	 * @param bytes
	 *            4���ֽڵ��ֽ�����
	 * @return һ����������
	 */
	public static int byteToInt(byte[] bytes) {
		int num = 0;
		int temp;
		temp = (0x000000ff & (bytes[0])) << 0;
		num = num | temp;
		temp = (0x000000ff & (bytes[1])) << 8;
		num = num | temp;
		temp = (0x000000ff & (bytes[2])) << 16;
		num = num | temp;
		temp = (0x000000ff & (bytes[3])) << 24;
		num = num | temp;
		return num;
	}
}
