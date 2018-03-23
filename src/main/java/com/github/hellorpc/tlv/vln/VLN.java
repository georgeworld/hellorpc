/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.vln;

import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.DataFormatException;
import com.github.hellorpc.util.MsgPackException;

/**
 * 变长数值处理工具类<br/>
 * V=Variable, L=Length，N=Numerical <br/>
 * 变长+长度+数字 数据格式是：“第一个字节：0+数值(用7个bits表示数值)” 或 <br/>
 * “第一个字节：1 + 数字字节数(用7个bits表示后续字节数)” + “后续字节：数值本身值的字节”
 *
 * @author George <Georgeinfo@163.com>
 */
public class VLN {

    private static byte[] getVariableLengthNumericalData(byte[] numericalData) {
        int baLen = numericalData.length;
        //将首字节的bit8设置成1
        int firstByte = baLen | 0x80; //0x80 = 0b10000000 = 128;
        byte[] data = new byte[baLen + 1];
        data[0] = (byte) firstByte;
        System.arraycopy(numericalData, 0, data, 1, baLen);
        return data;
    }

    /**
     * 将长度值，转换成变长字报文字节数组
     *
     * @param numerical 数值(int64长整型)
     * @return 转换后的VLN数据的byte[]数组
     */
    public static byte[] long2Vln(long numerical) {
        if (numerical < 0) {
            throw new DataFormatException("## The long value cannot be negative.");
        }
        byte[] ba = BinaryTool.long2byte(numerical);
        if (numerical <= 127) {
            return ba;
        } else {
            return getVariableLengthNumericalData(ba);
        }
    }

    /**
     * 将长度值，转换成变长字报文字节数组
     *
     * @param numerical 数值(int32整型)
     * @return 转换后的VLN数据的byte[]数组
     */
    public static byte[] int2Vln(int numerical) {
        if (numerical < 0) {
            throw new DataFormatException("## The int32 value cannot be negative.");
        }
        byte[] ba = BinaryTool.int2byte(numerical);
        if (numerical <= 127) {
            return ba;
        } else {
            return getVariableLengthNumericalData(ba);
        }
    }

    /**
     * 将长整形实际占用的字节数组，还原回长整形数字
     *
     * @param vlnData 待被还原的长整形所实际占用的字节数组
     * @return 还原后的长整形数值
     * @throws com.github.hellorpc.util.MsgPackException
     */
    public static long vln2long(byte[] vlnData) throws MsgPackException {
        return new Vln2NumericalConverter(vlnData, 2).getLongValue();
    }

    /**
     * 将长整形实际占用的字节数组，还原回长整形数字
     *
     * @param vlnData 待被还原的长整形所实际占用的字节数组
     * @return 还原后的长整形数值
     * @throws com.github.hellorpc.util.MsgPackException
     */
    public static int vln2int(byte[] vlnData) throws MsgPackException {
        return new Vln2NumericalConverter(vlnData, 1).getIntValue();
    }

    public static void main(String[] args) throws MsgPackException {
//        long length = 569;
//        System.out.println("#数据长度：" + length);
//
//        byte[] ba = long2Vln(length);
//        String bin = BinaryTool.toBinaryString(ba);
//        System.out.println("#数据的二进制格式：" + bin);
//
//        length = vln2long(ba);
//        System.out.println("#还原回来的数据长度：" + length);
        
        
        int length = Integer.MAX_VALUE;
        System.out.println("#数据长度：" + length);

        byte[] ba = int2Vln(length);
        String bin = BinaryTool.toBinaryString(ba);
        System.out.println("#数据的二进制格式：" + bin);

        length = vln2int(ba);
        System.out.println("#还原回来的数据长度：" + length);

    }
}
