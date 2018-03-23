/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.vln;

import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.DataFormatException;

/**
 * VLN转int/long的转换器
 *
 * @author George <Georgeinfo@163.com>
 */
public class Vln2NumericalConverter {
    
    public static void main(String[] args){
        byte[] b = VLN.int2Vln(Integer.MAX_VALUE);
        System.out.println(BinaryTool.toBinaryString(b));
    }

    private int intValue;
    private long longValue;

    public Vln2NumericalConverter(byte[] vlnData, int numericalType) throws DataFormatException {
        if (vlnData == null || vlnData.length == 0) {
            throw new DataFormatException("## Byte array can't be null or empty.");
        }
        byte firstByte = vlnData[0];
        int firstByteInt = ((int) firstByte) & 0xFF;
        if (firstByteInt >= 0x80) {//0x80 = 0b10000000 = 128，表示VLN的数值占用空间多于一个字节
            //数据长度所占的字节数
            int nummericalBytesCount = (((firstByteInt << 1) & 0xFF) >> 1) & 0xFF;
            if (vlnData.length - 1 < nummericalBytesCount) {
                throw new DataFormatException("## Bytes length can't less than:" + (nummericalBytesCount));
            } else {
                byte[] dataBytes = new byte[nummericalBytesCount];
                System.arraycopy(vlnData, 1, dataBytes, 0, nummericalBytesCount);

                if (numericalType == 1) {//int32
                    //将byte[]转成int32
                    int value = BinaryTool.byte2int(dataBytes);
                    //回传int32的值
                    this.intValue = value;
                } else if (numericalType == 2) {
                    //将byte[]转成int64
                    long value = BinaryTool.byte2long(dataBytes);
                    //回传int64的值
                    this.longValue = value;
                } else {
                    throw new DataFormatException("## Numerical type error,it must be on of {int or long}.");
                }
            }
        } else {//第一个字节的bit1-bit7，已经足够表示数值
            if (numericalType == 1) {
                //回传int32的值
                this.intValue = vlnData[0];
            } else if (numericalType == 2) {
                //回传int64的值
                this.longValue = vlnData[0];
            } else {
                throw new DataFormatException("## Numerical type error,it must be on of {int or long}.");
            }
        }
    }

    public int getIntValue() {
        return intValue;
    }

    public long getLongValue() {
        return longValue;
    }
}
