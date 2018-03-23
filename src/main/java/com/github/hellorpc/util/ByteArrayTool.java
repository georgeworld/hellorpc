package com.github.hellorpc.util;

/**
 *
 * @author dev1
 */
public class ByteArrayTool {

    public static byte[] concat(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            if (array == null) {
                continue;
            }
            length += array.length;
        }
        byte[] rs = new byte[length];
        int position = 0;
        for (byte[] array : arrays) {
            if (array == null) {
                continue;
            }
            System.arraycopy(array, 0, rs, position, array.length);
            position += array.length;
        }
        return rs;
    }
    
}
