package com.github.hellorpc.util;

import java.util.Arrays;

/**
 * BCD工具类
 *
 * @author George <Georgeinfo@163.com>
 */
public class BcdUtil {

    /**
     * BCD码转为10进制数字字符串
     *
     * @param bytes BCD码字节数组
     * @return 转换得到的10进制数字字符串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuilder temp = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    /**
     * 10进制串转为BCD码
     *
     * @param decimalString 10进制数字字符串
     * @return 转换得到的BCD码字节数组
     */
    public static byte[] str2Bcd(String decimalString) {
        return data2Bcd(decimalString.getBytes());
    }

    /**
     * 10进制数字字符串的ASCII编码字节数组，转为BCD码数据
     *
     * @param data 10进制数字字符串的数据（ASCII编码）
     * @return 转换得到的BCD码字节数组（如果传入的原始数据是0开头，则转换后的数字字符串，<br/>
     * 会丢失最左侧的0，但是对于终归要转为数字的数字字符串来说，丢失最左边的0，不影响数字数值大小）
     */
    public static byte[] data2Bcd(byte[] data) {
        byte[] rawData;
        int len = data.length;
        int mod = len % 2;
        if (mod != 0) {//如果字节数量是单数，需要左补0组成偶数个字节，才能做BCD压缩
            rawData = new byte[len + 1];
            rawData[0] = 48; //48是字符0的ASCII编码
            System.arraycopy(data, 0, rawData, 1, len);
            len = len + 1;
        } else {
            rawData = data;
        }
        int rawDataLen = len; //rawData.length;
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        int j, k;
        for (int p = 0; p < rawDataLen / 2; p++) {
            if ((rawData[2 * p] >= '0') && (rawData[2 * p] <= '9')) {
                j = rawData[2 * p] - '0';
            } else if ((rawData[2 * p] >= 'a') && (rawData[2 * p] <= 'z')) {
                j = rawData[2 * p] - 'a' + 0x0a;
            } else {
                j = rawData[2 * p] - 'A' + 0x0a;
            }
            if ((rawData[2 * p + 1] >= '0') && (rawData[2 * p + 1] <= '9')) {
                k = rawData[2 * p + 1] - '0';
            } else if ((rawData[2 * p + 1] >= 'a') && (rawData[2 * p + 1] <= 'z')) {
                k = rawData[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = rawData[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    public static void main(String[] args) {
        String num = "1234567890";
        byte[] bs = BcdUtil.str2Bcd(num);
        System.out.println(Arrays.toString(bs));
        System.out.println((byte)987);
//        System.err.println("## 原始数据：" + num);
//        byte[] data = str2Bcd(num);
//        System.out.println("## BCD压缩后的数据：" + BinaryTool.toBinaryString(data));
//        num = bcd2Str(data);
//        System.err.println("## 还原后的数据：" + num);
    }
}
