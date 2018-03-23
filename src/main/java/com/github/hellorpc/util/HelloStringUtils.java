/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.util;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class HelloStringUtils {
    /**
     * 替换指定位置的子串
     *
     * @param text 主字符串
     * @param substring 用来替换主字符串某个区段的子串
     * @param beginIndex 主字符串被替换区段的开始索引值
     * @param endIndex 主字符串被替换区段的结束索引值
     * @return 替换完成后的主字符串
     */
    public static String replaceSubString(String text, String substring, int beginIndex, int endIndex) {
        if (text == null
                || text.trim().isEmpty()
                || substring == null
                || substring.trim().isEmpty()) {
            return text;
        }

        if (text.equals(substring)) {
            return text;
        }

        int textLength = text.length();
        int substringLen = substring.length();

        if (beginIndex >= endIndex || endIndex >= textLength) {
            throw new RuntimeException("## Endding index can't bigger than length of text.");
        }

        if (substringLen != (endIndex - beginIndex + 1)) {
            throw new RuntimeException("## Index range is not equal to the length of the sub string.");
        }

        StringBuilder strb = new StringBuilder(textLength);
        if (beginIndex == 0) {
            strb.append(substring).append(text.substring(endIndex + 1));
            return strb.toString();
        }

        if (endIndex == textLength - 1) {
            strb.append(text.substring(0, beginIndex)).append(substring);
            return strb.toString();
        }

        strb.append(text.substring(0, beginIndex)).append(substring).append(text.substring(endIndex + 1));
        return strb.toString();
    }

    public static void main(String[] args) {
        System.out.println(replaceSubString("112233445566", "88", 2, 3));
//        String text = "abcdefghijklmnopqrstuvwxyz";
//        String substring = "你好吗";
//        System.out.println("替换前的字符串：" + text + "，替换前的字符串长度：" + text.length());
//        long begin = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            text = replaceSubString(text, substring, 23, 25);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("####循环10000次耗时：" + (end - begin));
//        System.out.println("替换后的字符串：" + text + "，替换后的字符串长度：" + text.length());
    }
}
