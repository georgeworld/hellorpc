/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.util;

import java.nio.ByteBuffer;

/**
 * 二进制(byte[])工具类
 *
 * @author George <Georgeinfo@163.com>
 */
public class BinaryTool {
    
    public static class Data {
        int i ;

        public Data(int i) {
            this.i = i;
        }

        public Data() {
        }

        
        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
        
    }
    
//    public static void main(String[] args) throws Exception{
//        ObjectMapper mapper = new ObjectMapper();
//        ArrayList<Data> lists = new ArrayList<Data>();
//        lists.add(new Data(1));lists.add(new Data(2));lists.add(new Data(3));
//        String json = mapper.writeValueAsString(lists);
//        System.out.println(json);
//        Object obj = mapper.readValue(json, lists.getClass());
//        ArrayList<Data> d = (ArrayList<Data>)obj;
//        System.out.println(d.get(0).getClass());
//    }

    /**
     * 高效的int -> byte[]转化<br/>
     * 不足4个字节(32bit位)时，会左补全零空字节
     */
    public static byte[] intToBytesFullBits(final int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++) {
            byteArray[3 - n] = (byte) (integer >>> (n * 8));
        }

        return (byteArray);
    }

    /**
     * 高效的 byte[] -> int 转化
     */
    public static int bytesToIntFullBits(byte[] b) {
        return bytesToIntFullBits(b, 0);
    }

    /**
     * 高效的 byte[] -> int 转化
     */
    public static int bytesToIntFullBits(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    /**
     * 高效的 byte[] -> long 转化<br/>
     * 不足8个字节(64bit位)时，会左补全零空字节
     */
    public static byte[] long2BytesFullBits(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * 高效byte[] -> long工具方法
     */
    public static long bytes2LongFullBits(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    /**
     * 打印每个字节的bits构成
     */
    public static String toBinaryString(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        StringBuilder fullBinText = new StringBuilder();
        for (byte b : ba) {
            String binString = Integer.toBinaryString(((int) b) & 0xFF);
            if (binString.length() < 8) {
                int len = (8 - binString.length());
                StringBuilder strb = new StringBuilder(8);
                for (int i = 0; i < len; i++) {
                    strb.append("0");
                }
                strb.append(binString);
                binString = strb.toString();

            }

            fullBinText.append(binString).append(",");
        }

        return fullBinText.deleteCharAt(fullBinText.length() - 1).toString();
    }

    /**
     * 为byte[]左补0到指定的总长度
     *
     * @param ba 待被左补0的字节数组
     * @param requiredSize 左补0后需要达到的字节数组长度
     * @return 左补0后的数据
     */
    public static byte[] leftPadZero(byte[] ba, int requiredSize) {
        if (ba == null || ba.length == 0) {
            return new byte[requiredSize];
        } else {
            int baLen = ba.length;
            int missingSize = requiredSize - baLen;
            byte[] data = new byte[requiredSize];
            System.arraycopy(new byte[missingSize], 0, data, 0, missingSize);
            System.arraycopy(ba, 0, data, missingSize, baLen);

            return data;
        }
    }

    /**
     * Converts an integer into a byte array of hex<br/>
     * 将一个整数转换成它所实际占用的字节数组<br/>
     * 如果是负数：固定占用四个字节<br/>
     * 如果是正数：数值实际占用几个字节，就返回几个字节，实际数值不足4个字节时，不会在左边补全0的空字节。
     *
     * @param value 待被转换的整数(int32)
     * @return bytes representation of integer，转换后的字节数组
     */
    public static byte[] int2byte(int value) {
        if (value < 0) {
            return new byte[]{
                (byte) (value >>> 24 & 0xFF),
                (byte) ((value >>> 16) & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else if (value <= 0xFF) {
            return new byte[]{(byte) (value & 0xFF)};
        } else if (value <= 0xFFFF) {
            return new byte[]{(byte) (value >>> 8 & 0xFF), (byte) ((value) & 0xFF)};
        } else if (value <= 0xFFFFFF) {
            return new byte[]{(byte) (value >>> 16 & 0xFF), (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else {
            return new byte[]{(byte) (value >>> 24 & 0xFF), (byte) ((value >>> 16) & 0xFF),
                (byte) ((value >>> 8) & 0xFF), (byte) ((value) & 0xFF)};
        }
    }

    /**
     * Converts a byte array of hex into an integer<br/>
     * 将一个整数(int32)的实际占用字节数组，还原成整数(int32)
     *
     * @param bytes
     * @return integer representation of bytes
     */
    public static int byte2int(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        //如果传进来的字节数组，不足4个字节，则左补全零空字节
        for (int i = 0; i < (4 - bytes.length); i++) {
            byteBuffer.put((byte) 0);
        }
        for (int i = 0; i < bytes.length; i++) {
            byteBuffer.put(bytes[i]);
        }
        byteBuffer.position(0);
        return byteBuffer.getInt();
    }

    /**
     * Converts an long integer into a byte array of hex<br/>
     * 将一个长整形(int64)，转换成它实际占用的字节数组<br/>
     * 对于负数：固定占用8个字节<br/>
     * 对于正数：转换后的字节数组，是其数值实际占用的字节数量，当数值不足8个字节时<br/>
     * 不会在左侧补全零空字节。
     *
     * @param value
     * @return bytes representation of integer
     */
    public static byte[] long2byte(long value) {
        if (value < 0L) {
            return new byte[]{ //8个字节
                (byte) (value >>> 56 & 0xFF),
                (byte) (value >>> 48 & 0xFF),
                (byte) (value >>> 40 & 0xFF),
                (byte) (value >>> 32 & 0xFF),
                (byte) (value >>> 24 & 0xFF),
                (byte) ((value >>> 16) & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else if (value <= 0xFFL) { //1个字节，末尾的L，是用来定义这个数值是“Long长整形”，这个L一定不能少，下同
            return new byte[]{(byte) (value & 0xFF)};
        } else if (value <= 0xFFFFL) { //2个字节
            return new byte[]{(byte) (value >>> 8 & 0xFF), (byte) ((value) & 0xFF)};
        } else if (value <= 0xFFFFFFL) { //3个字节
            return new byte[]{(byte) (value >>> 16 & 0xFF), (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else if (value <= 0xFFFFFFFFL) { //4个字节
            return new byte[]{
                (byte) (value >>> 24 & 0xFF),
                (byte) (value >>> 16 & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else if (value <= 0xFFFFFFFFFFL) { //5个字节
            return new byte[]{
                (byte) (value >>> 32 & 0xFF),
                (byte) (value >>> 24 & 0xFF),
                (byte) (value >>> 16 & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else if (value <= 0xFFFFFFFFFFFFL) { //6个字节
            return new byte[]{
                (byte) (value >>> 32 & 0xFF),
                (byte) (value >>> 24 & 0xFF),
                (byte) (value >>> 16 & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else if (value <= 0xFFFFFFFFFFFFFFL) { //7个字节
            return new byte[]{
                (byte) (value >>> 40 & 0xFF),
                (byte) (value >>> 32 & 0xFF),
                (byte) (value >>> 24 & 0xFF),
                (byte) (value >>> 16 & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        } else {
            return new byte[]{
                (byte) (value >>> 56 & 0xFF),
                (byte) (value >>> 48 & 0xFF),
                (byte) (value >>> 40 & 0xFF),
                (byte) (value >>> 32 & 0xFF),
                (byte) (value >>> 24 & 0xFF),
                (byte) ((value >>> 16) & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) ((value) & 0xFF)};
        }
    }

    /**
     * Converts a byte array of hex into an long integer
     * 将一个长整数(int64)的实际占用字节数组，还原成长整数(int64)
     *
     * @param bytes 待被转换的长整形数值占用的字节数组
     * @return integer representation of bytes
     */
    public static long byte2long(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (int i = 0; i < (8 - bytes.length); i++) {
            byteBuffer.put((byte) 0);
        }
        for (int i = 0; i < bytes.length; i++) {
            byteBuffer.put(bytes[i]);
        }
        byteBuffer.position(0);
        return byteBuffer.getLong();
    }

//    public static void main(String[] args) throws DecoderException {
//
//        int num = 0x9f06;
//        byte[] ba = intToBytesFullBits(num);
//        System.out.println("## 的字节数是：" + ba.length + "，字节内容是：" + Hex.encodeHexString(ba));
//        byte[] fuck = Hex.decodeHex("9F06".toCharArray());
//        System.out.println(BinaryTool.byte2int(fuck));
//
//    }
}
