package com.github.hellorpc.nio.client;

import java.awt.Color;

/**
 * UI������
 * 
 * @author XUHAILIN730
 * 
 */
public final class UIUtil {
	private UIUtil() {
	}

	/**
	 * ��16���Ʊ�ʾ���ַ�������Color
	 * 
	 * @param str
	 *            ��ɫ����#3e62a6
	 * @return
	 */
	public static Color parseColor(String str) {
		int i = Integer.parseInt(str.substring(1), 16);
		return new Color(i);
	}

	/**
	 * �õ�Color�����16�����ַ�����ʾ
	 * @param c
	 * @return
	 */
	public static String colorToString(Color c) {
		String r = Integer.toHexString(c.getRed());
		r = r.length() < 2 ? ('0' + r) : r;
		String b = Integer.toHexString(c.getBlue());
		b = b.length() < 2 ? ('0' + b) : b;
		String g = Integer.toHexString(c.getGreen());
		g = g.length() < 2 ? ('0' + g) : g;
		return '#' + r + b + g;
	}
}
