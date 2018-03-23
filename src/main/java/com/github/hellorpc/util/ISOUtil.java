/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2014 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.hellorpc.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

/**
 * various functions needed to pack/unpack ISO-8583 fields
 *
 * @author apr@jpos.org
 * @author Hani S. Kirollos
 * @author Alwyn Schoeman
 * @version $Id$
 * @see ISOComponent
 */
@SuppressWarnings("unused")
public class ISOUtil {

    /**
     * All methods in this class are static, so there's usually no need to
     * instantiate it We provide this public constructor in order to deal with
     * some legacy script integration that needs an instance of this class in a
     * rendering context.
     */
    public ISOUtil() {
        super();
    }
    public static final String[] hexStrings;

    static {
        hexStrings = new String[256];
        for (int i = 0; i < 256; i++) {
            StringBuilder d = new StringBuilder(2);
            char ch = Character.forDigit(((byte) i >> 4) & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            ch = Character.forDigit((byte) i & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            hexStrings[i] = d.toString();
        }

    }

    /**
     * Default encoding (charset) for bytes transmissions over network
     *
     * @deprecated use {@link #CHARSET} instead
     */
    public static final String ENCODING = "ISO8859_1";

    /**
     * Default charset for bytes transmissions over network
     */
    public static final Charset CHARSET = Charset.forName("ISO8859_1");

    public static final byte[] EBCDIC2ASCII = new byte[]{
        (byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3,
        (byte) 0x9C, (byte) 0x9, (byte) 0x86, (byte) 0x7F,
        (byte) 0x97, (byte) 0x8D, (byte) 0x8E, (byte) 0xB,
        (byte) 0xC, (byte) 0xD, (byte) 0xE, (byte) 0xF,
        (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
        (byte) 0x9D, (byte) 0xA, (byte) 0x8, (byte) 0x87,
        (byte) 0x18, (byte) 0x19, (byte) 0x92, (byte) 0x8F,
        (byte) 0x1C, (byte) 0x1D, (byte) 0x1E, (byte) 0x1F,
        (byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x83,
        (byte) 0x84, (byte) 0x85, (byte) 0x17, (byte) 0x1B,
        (byte) 0x88, (byte) 0x89, (byte) 0x8A, (byte) 0x8B,
        (byte) 0x8C, (byte) 0x5, (byte) 0x6, (byte) 0x7,
        (byte) 0x90, (byte) 0x91, (byte) 0x16, (byte) 0x93,
        (byte) 0x94, (byte) 0x95, (byte) 0x96, (byte) 0x4,
        (byte) 0x98, (byte) 0x99, (byte) 0x9A, (byte) 0x9B,
        (byte) 0x14, (byte) 0x15, (byte) 0x9E, (byte) 0x1A,
        (byte) 0x20, (byte) 0xA0, (byte) 0xE2, (byte) 0xE4,
        (byte) 0xE0, (byte) 0xE1, (byte) 0xE3, (byte) 0xE5,
        (byte) 0xE7, (byte) 0xF1, (byte) 0xA2, (byte) 0x2E,
        (byte) 0x3C, (byte) 0x28, (byte) 0x2B, (byte) 0x7C,
        (byte) 0x26, (byte) 0xE9, (byte) 0xEA, (byte) 0xEB,
        (byte) 0xE8, (byte) 0xED, (byte) 0xEE, (byte) 0xEF,
        (byte) 0xEC, (byte) 0xDF, (byte) 0x21, (byte) 0x24,
        (byte) 0x2A, (byte) 0x29, (byte) 0x3B, (byte) 0x5E,
        (byte) 0x2D, (byte) 0x2F, (byte) 0xC2, (byte) 0xC4,
        (byte) 0xC0, (byte) 0xC1, (byte) 0xC3, (byte) 0xC5,
        (byte) 0xC7, (byte) 0xD1, (byte) 0xA6, (byte) 0x2C,
        (byte) 0x25, (byte) 0x5F, (byte) 0x3E, (byte) 0x3F,
        (byte) 0xF8, (byte) 0xC9, (byte) 0xCA, (byte) 0xCB,
        (byte) 0xC8, (byte) 0xCD, (byte) 0xCE, (byte) 0xCF,
        (byte) 0xCC, (byte) 0x60, (byte) 0x3A, (byte) 0x23,
        (byte) 0x40, (byte) 0x27, (byte) 0x3D, (byte) 0x22,
        (byte) 0xD8, (byte) 0x61, (byte) 0x62, (byte) 0x63,
        (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x67,
        (byte) 0x68, (byte) 0x69, (byte) 0xAB, (byte) 0xBB,
        (byte) 0xF0, (byte) 0xFD, (byte) 0xFE, (byte) 0xB1,
        (byte) 0xB0, (byte) 0x6A, (byte) 0x6B, (byte) 0x6C,
        (byte) 0x6D, (byte) 0x6E, (byte) 0x6F, (byte) 0x70,
        (byte) 0x71, (byte) 0x72, (byte) 0xAA, (byte) 0xBA,
        (byte) 0xE6, (byte) 0xB8, (byte) 0xC6, (byte) 0xA4,
        (byte) 0xB5, (byte) 0x7E, (byte) 0x73, (byte) 0x74,
        (byte) 0x75, (byte) 0x76, (byte) 0x77, (byte) 0x78,
        (byte) 0x79, (byte) 0x7A, (byte) 0xA1, (byte) 0xBF,
        (byte) 0xD0, (byte) 0x5B, (byte) 0xDE, (byte) 0xAE,
        (byte) 0xAC, (byte) 0xA3, (byte) 0xA5, (byte) 0xB7,
        (byte) 0xA9, (byte) 0xA7, (byte) 0xB6, (byte) 0xBC,
        (byte) 0xBD, (byte) 0xBE, (byte) 0xDD, (byte) 0xA8,
        (byte) 0xAF, (byte) 0x5D, (byte) 0xB4, (byte) 0xD7,
        (byte) 0x7B, (byte) 0x41, (byte) 0x42, (byte) 0x43,
        (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47,
        (byte) 0x48, (byte) 0x49, (byte) 0xAD, (byte) 0xF4,
        (byte) 0xF6, (byte) 0xF2, (byte) 0xF3, (byte) 0xF5,
        (byte) 0x7D, (byte) 0x4A, (byte) 0x4B, (byte) 0x4C,
        (byte) 0x4D, (byte) 0x4E, (byte) 0x4F, (byte) 0x50,
        (byte) 0x51, (byte) 0x52, (byte) 0xB9, (byte) 0xFB,
        (byte) 0xFC, (byte) 0xF9, (byte) 0xFA, (byte) 0xFF,
        (byte) 0x5C, (byte) 0xF7, (byte) 0x53, (byte) 0x54,
        (byte) 0x55, (byte) 0x56, (byte) 0x57, (byte) 0x58,
        (byte) 0x59, (byte) 0x5A, (byte) 0xB2, (byte) 0xD4,
        (byte) 0xD6, (byte) 0xD2, (byte) 0xD3, (byte) 0xD5,
        (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33,
        (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37,
        (byte) 0x38, (byte) 0x39, (byte) 0xB3, (byte) 0xDB,
        (byte) 0xDC, (byte) 0xD9, (byte) 0xDA, (byte) 0x9F
    };
    public static final byte[] ASCII2EBCDIC = new byte[]{
        (byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3,
        (byte) 0x37, (byte) 0x2D, (byte) 0x2E, (byte) 0x2F,
        (byte) 0x16, (byte) 0x5, (byte) 0x15, (byte) 0xB,
        (byte) 0xC, (byte) 0xD, (byte) 0xE, (byte) 0xF,
        (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13,
        (byte) 0x3C, (byte) 0x3D, (byte) 0x32, (byte) 0x26,
        (byte) 0x18, (byte) 0x19, (byte) 0x3F, (byte) 0x27,
        (byte) 0x1C, (byte) 0x1D, (byte) 0x1E, (byte) 0x1F,
        (byte) 0x40, (byte) 0x5A, (byte) 0x7F, (byte) 0x7B,
        (byte) 0x5B, (byte) 0x6C, (byte) 0x50, (byte) 0x7D,
        (byte) 0x4D, (byte) 0x5D, (byte) 0x5C, (byte) 0x4E,
        (byte) 0x6B, (byte) 0x60, (byte) 0x4B, (byte) 0x61,
        (byte) 0xF0, (byte) 0xF1, (byte) 0xF2, (byte) 0xF3,
        (byte) 0xF4, (byte) 0xF5, (byte) 0xF6, (byte) 0xF7,
        (byte) 0xF8, (byte) 0xF9, (byte) 0x7A, (byte) 0x5E,
        (byte) 0x4C, (byte) 0x7E, (byte) 0x6E, (byte) 0x6F,
        (byte) 0x7C, (byte) 0xC1, (byte) 0xC2, (byte) 0xC3,
        (byte) 0xC4, (byte) 0xC5, (byte) 0xC6, (byte) 0xC7,
        (byte) 0xC8, (byte) 0xC9, (byte) 0xD1, (byte) 0xD2,
        (byte) 0xD3, (byte) 0xD4, (byte) 0xD5, (byte) 0xD6,
        (byte) 0xD7, (byte) 0xD8, (byte) 0xD9, (byte) 0xE2,
        (byte) 0xE3, (byte) 0xE4, (byte) 0xE5, (byte) 0xE6,
        (byte) 0xE7, (byte) 0xE8, (byte) 0xE9, (byte) 0xAD,
        (byte) 0xE0, (byte) 0xBD, (byte) 0x5F, (byte) 0x6D,
        (byte) 0x79, (byte) 0x81, (byte) 0x82, (byte) 0x83,
        (byte) 0x84, (byte) 0x85, (byte) 0x86, (byte) 0x87,
        (byte) 0x88, (byte) 0x89, (byte) 0x91, (byte) 0x92,
        (byte) 0x93, (byte) 0x94, (byte) 0x95, (byte) 0x96,
        (byte) 0x97, (byte) 0x98, (byte) 0x99, (byte) 0xA2,
        (byte) 0xA3, (byte) 0xA4, (byte) 0xA5, (byte) 0xA6,
        (byte) 0xA7, (byte) 0xA8, (byte) 0xA9, (byte) 0xC0,
        (byte) 0x4F, (byte) 0xD0, (byte) 0xA1, (byte) 0x7,
        (byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23,
        (byte) 0x24, (byte) 0x25, (byte) 0x6, (byte) 0x17,
        (byte) 0x28, (byte) 0x29, (byte) 0x2A, (byte) 0x2B,
        (byte) 0x2C, (byte) 0x9, (byte) 0xA, (byte) 0x1B,
        (byte) 0x30, (byte) 0x31, (byte) 0x1A, (byte) 0x33,
        (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x8,
        (byte) 0x38, (byte) 0x39, (byte) 0x3A, (byte) 0x3B,
        (byte) 0x4, (byte) 0x14, (byte) 0x3E, (byte) 0xFF,
        (byte) 0x41, (byte) 0xAA, (byte) 0x4A, (byte) 0xB1,
        (byte) 0x9F, (byte) 0xB2, (byte) 0x6A, (byte) 0xB5,
        (byte) 0xBB, (byte) 0xB4, (byte) 0x9A, (byte) 0x8A,
        (byte) 0xB0, (byte) 0xCA, (byte) 0xAF, (byte) 0xBC,
        (byte) 0x90, (byte) 0x8F, (byte) 0xEA, (byte) 0xFA,
        (byte) 0xBE, (byte) 0xA0, (byte) 0xB6, (byte) 0xB3,
        (byte) 0x9D, (byte) 0xDA, (byte) 0x9B, (byte) 0x8B,
        (byte) 0xB7, (byte) 0xB8, (byte) 0xB9, (byte) 0xAB,
        (byte) 0x64, (byte) 0x65, (byte) 0x62, (byte) 0x66,
        (byte) 0x63, (byte) 0x67, (byte) 0x9E, (byte) 0x68,
        (byte) 0x74, (byte) 0x71, (byte) 0x72, (byte) 0x73,
        (byte) 0x78, (byte) 0x75, (byte) 0x76, (byte) 0x77,
        (byte) 0xAC, (byte) 0x69, (byte) 0xED, (byte) 0xEE,
        (byte) 0xEB, (byte) 0xEF, (byte) 0xEC, (byte) 0xBF,
        (byte) 0x80, (byte) 0xFD, (byte) 0xFE, (byte) 0xFB,
        (byte) 0xFC, (byte) 0xBA, (byte) 0xAE, (byte) 0x59,
        (byte) 0x44, (byte) 0x45, (byte) 0x42, (byte) 0x46,
        (byte) 0x43, (byte) 0x47, (byte) 0x9C, (byte) 0x48,
        (byte) 0x54, (byte) 0x51, (byte) 0x52, (byte) 0x53,
        (byte) 0x58, (byte) 0x55, (byte) 0x56, (byte) 0x57,
        (byte) 0x8C, (byte) 0x49, (byte) 0xCD, (byte) 0xCE,
        (byte) 0xCB, (byte) 0xCF, (byte) 0xCC, (byte) 0xE1,
        (byte) 0x70, (byte) 0xDD, (byte) 0xDE, (byte) 0xDB,
        (byte) 0xDC, (byte) 0x8D, (byte) 0x8E, (byte) 0xDF
    };
    public static final byte STX = 0x02;
    public static final byte FS = 0x1C;
    public static final byte US = 0x1F;
    public static final byte RS = 0x1D;
    public static final byte GS = 0x1E;
    public static final byte ETX = 0x03;

    public static String ebcdicToAscii(byte[] e) {
        return new String(
                ebcdicToAsciiBytes(e, 0, e.length), CHARSET
        );
    }

    public static String ebcdicToAscii(byte[] e, int offset, int len) {
        return new String(
                ebcdicToAsciiBytes(e, offset, len), CHARSET
        );
    }

    public static byte[] ebcdicToAsciiBytes(byte[] e) {
        return ebcdicToAsciiBytes(e, 0, e.length);
    }

    public static byte[] ebcdicToAsciiBytes(byte[] e, int offset, int len) {
        byte[] a = new byte[len];
        for (int i = 0; i < len; i++) {
            a[i] = EBCDIC2ASCII[e[offset + i] & 0xFF];
        }
        return a;
    }

    public static byte[] asciiToEbcdic(String s) {
        int len = s.length();
        byte b[] = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = ASCII2EBCDIC[s.charAt(i) & 0xFF];
        }
        return b;
    }

    public static byte[] asciiToEbcdic(byte[] a) {
        byte[] e = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            e[i] = ASCII2EBCDIC[a[i] & 0xFF];
        }
        return e;
    }

    public static void asciiToEbcdic(String s, byte[] e, int offset) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            e[offset + i] = ASCII2EBCDIC[s.charAt(i) & 0xFF];
        }
    }

    public static void asciiToEbcdic(byte[] s, byte[] e, int offset) {
        int len = s.length;
        for (int i = 0; i < len; i++) {
            e[offset + i] = ASCII2EBCDIC[s[i]];
        }
    }

    /**
     * pads to the right
     *
     * @param s - original string
     * @param len - desired len
     * @return space padded string
     */
    public static String strpad(String s, int len) {
        StringBuilder d = new StringBuilder(s);
        while (d.length() < len) {
            d.append(' ');
        }
        return d.toString();
    }

    public static String zeropadRight(String s, int len) {
        StringBuilder d = new StringBuilder(s);
        while (d.length() < len) {
            d.append('0');
        }
        return d.toString();
    }

    /**
     * converts to BCD
     *
     * @param s - the number
     * @param padLeft - flag indicating left/right padding
     * @param d The byte array to copy into.
     * @param offset Where to start copying into.
     * @return BCD representation of the number
     */
    public static byte[] str2bcd(String s, boolean padLeft, byte[] d, int offset) {
        int len = s.length();
        int start = (((len & 1) == 1) && padLeft) ? 1 : 0;
        for (int i = start; i < len + start; i++) {
            d[offset + (i >> 1)] |= (s.charAt(i - start) - '0') << ((i & 1) == 1 ? 0 : 4);
        }
        return d;
    }

    /**
     * converts to BCD
     *
     * @param s - the number
     * @param padLeft - flag indicating left/right padding
     * @return BCD representation of the number
     */
    public static byte[] str2bcd(String s, boolean padLeft) {
        int len = s.length();
        byte[] d = new byte[(len + 1) >> 1];
        return str2bcd(s, padLeft, d, 0);
    }

    /**
     * converts to BCD
     *
     * @param s - the number
     * @param padLeft - flag indicating left/right padding
     * @param fill - fill value
     * @return BCD representation of the number
     */
    public static byte[] str2bcd(String s, boolean padLeft, byte fill) {
        int len = s.length();
        byte[] d = new byte[(len + 1) >> 1];
        Arrays.fill(d, fill);
        int start = (((len & 1) == 1) && padLeft) ? 1 : 0;
        for (int i = start; i < len + start; i++) {
            d[i >> 1] |= (s.charAt(i - start) - '0') << ((i & 1) == 1 ? 0 : 4);
        }
        return d;
    }

    /**
     * converts a BCD representation of a number to a String
     *
     * @param b - BCD representation
     * @param offset - starting offset
     * @param len - BCD field len
     * @param padLeft - was padLeft packed?
     * @return the String representation of the number
     */
    public static String bcd2str(byte[] b, int offset,
            int len, boolean padLeft) {
        StringBuilder d = new StringBuilder(len);
        int start = (((len & 1) == 1) && padLeft) ? 1 : 0;
        for (int i = start; i < len + start; i++) {
            int shift = ((i & 1) == 1 ? 0 : 4);
            char c = Character.forDigit(
                    ((b[offset + (i >> 1)] >> shift) & 0x0F), 16);
            if (c == 'd') {
                c = '=';
            }
            d.append(Character.toUpperCase(c));
        }
        return d.toString();
    }

    /**
     * converts a byte array to hex string (suitable for dumps and ASCII
     * packaging of Binary fields
     *
     * @param b - byte array
     * @return String representation
     */
    public static String hexString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            d.append(hexStrings[(int) aB & 0xFF]);
        }
        return d.toString();
    }

    /**
     * converts a byte array to printable characters
     *
     * @param b - byte array
     * @return String representation
     */
    public static String dumpString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            char c = (char) aB;
            if (Character.isISOControl(c)) {
                // TODO: complete list of control characters,
                // use a String[] instead of this weird switch
                switch (c) {
                    case '\r':
                        d.append("{CR}");
                        break;
                    case '\n':
                        d.append("{LF}");
                        break;
                    case '\000':
                        d.append("{NULL}");
                        break;
                    case '\001':
                        d.append("{SOH}");
                        break;
                    case '\002':
                        d.append("{STX}");
                        break;
                    case '\003':
                        d.append("{ETX}");
                        break;
                    case '\004':
                        d.append("{EOT}");
                        break;
                    case '\005':
                        d.append("{ENQ}");
                        break;
                    case '\006':
                        d.append("{ACK}");
                        break;
                    case '\007':
                        d.append("{BEL}");
                        break;
                    case '\020':
                        d.append("{DLE}");
                        break;
                    case '\025':
                        d.append("{NAK}");
                        break;
                    case '\026':
                        d.append("{SYN}");
                        break;
                    case '\034':
                        d.append("{FS}");
                        break;
                    case '\036':
                        d.append("{RS}");
                        break;
                    default:
                        d.append('[');
                        d.append(hexStrings[(int) aB & 0xFF]);
                        d.append(']');
                        break;
                }
            } else {
                d.append(c);
            }

        }
        return d.toString();
    }

    /**
     * converts a byte array to hex string (suitable for dumps and ASCII
     * packaging of Binary fields
     *
     * @param b - byte array
     * @param offset - starting position
     * @param len the length
     * @return String representation
     */
    public static String hexString(byte[] b, int offset, int len) {
        StringBuilder d = new StringBuilder(len * 2);
        len += offset;
        for (int i = offset; i < len; i++) {
            d.append(hexStrings[(int) b[i] & 0xFF]);
        }
        return d.toString();
    }

    /**
     * bit representation of a BitSet suitable for dumps and debugging
     *
     * @param b - the BitSet
     * @return string representing the bits (i.e. 011010010...)
     */
    public static String bitSet2String(BitSet b) {
        int len = b.size();
        len = (len > 128) ? 128 : len;
        StringBuilder d = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            d.append(b.get(i) ? '1' : '0');
        }
        return d.toString();
    }

    /**
     * converts a BitSet into a binary field used in pack routines
     *
     * @param b - the BitSet
     * @return binary representation
     */
    public static byte[] bitSet2byte(BitSet b) {
        int len = (((b.length() + 62) >> 6) << 6);
        byte[] d = new byte[len >> 3];
        for (int i = 0; i < len; i++) {
            if (b.get(i + 1)) {
                d[i >> 3] |= (0x80 >> (i % 8));
            }
        }
        if (len > 64) {
            d[0] |= 0x80;
        }
        if (len > 128) {
            d[8] |= 0x80;
        }
        return d;
    }

    /**
     * converts a BitSet into a binary field used in pack routines
     *
     * @param b - the BitSet
     * @param bytes - number of bytes to return
     * @return binary representation
     */
    public static byte[] bitSet2byte(BitSet b, int bytes) {
        int len = bytes * 8;

        byte[] d = new byte[bytes];
        for (int i = 0; i < len; i++) {
            if (b.get(i + 1)) {
                d[i >> 3] |= (0x80 >> (i % 8));
            }
        }
        //TODO: review why 2nd & 3rd bit map flags are set here??? 
        if (len > 64) {
            d[0] |= 0x80;
        }
        if (len > 128) {
            d[8] |= 0x80;
        }
        return d;
    }

    /*
     * Convert BitSet to int value.
     */
    public static int bitSet2Int(BitSet bs) {
        int total = 0;
        int b = bs.length() - 1;
        if (b > 0) {
            int value = (int) Math.pow(2, b);
            for (int i = 0; i <= b; i++) {
                if (bs.get(i)) {
                    total += value;
                }
                value = value >> 1;
            }
        }

        return total;
    }

    /*
     * Convert int value to BitSet.
     */
    public static BitSet int2BitSet(int value) {

        return int2BitSet(value, 0);
    }

    /*
     * Convert int value to BitSet.
     */
    public static BitSet int2BitSet(int value, int offset) {

        BitSet bs = new BitSet();

        String hex = Integer.toHexString(value);
        hex2BitSet(bs, hex.getBytes(), offset);

        return bs;
    }

    /**
     * Converts a binary representation of a Bitmap field into a Java BitSet
     *
     * @param b - binary representation
     * @param offset - staring offset
     * @param bitZeroMeansExtended - true for ISO-8583
     * @return java BitSet object
     */
    public static BitSet byte2BitSet(byte[] b, int offset, boolean bitZeroMeansExtended) {
        int len = bitZeroMeansExtended
                ? ((b[offset] & 0x80) == 0x80 ? 128 : 64) : 64;
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            if (((b[offset + (i >> 3)]) & (0x80 >> (i % 8))) > 0) {
                bmap.set(i + 1);
            }
        }
        return bmap;
    }

    /**
     * Converts a binary representation of a Bitmap field into a Java BitSet
     *
     * @param b - binary representation
     * @param offset - staring offset
     * @param maxBits - max number of bits (supports 64,128 or 192)
     * @return java BitSet object
     */
    public static BitSet byte2BitSet(byte[] b, int offset, int maxBits) {
        int len = maxBits > 64
                ? ((b[offset] & 0x80) == 0x80 ? 128 : 64) : maxBits;

        if (maxBits > 128
                && b.length > offset + 8
                && (b[offset + 8] & 0x80) == 0x80) {
            len = 192;
        }
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            if (((b[offset + (i >> 3)]) & (0x80 >> (i % 8))) > 0) {
                bmap.set(i + 1);
            }
        }
        return bmap;
    }

    /**
     * Converts a binary representation of a Bitmap field into a Java BitSet
     *
     * @param bmap - BitSet
     * @param b - hex representation
     * @param bitOffset - (i.e. 0 for primary bitmap, 64 for secondary)
     * @return java BitSet object
     */
    public static BitSet byte2BitSet(BitSet bmap, byte[] b, int bitOffset) {
        int len = b.length << 3;
        for (int i = 0; i < len; i++) {
            if (((b[i >> 3]) & (0x80 >> (i % 8))) > 0) {
                bmap.set(bitOffset + i + 1);
            }
        }
        return bmap;
    }

    /**
     * Converts an ASCII representation of a Bitmap field into a Java BitSet
     *
     * @param b - hex representation
     * @param offset - starting offset
     * @param bitZeroMeansExtended - true for ISO-8583
     * @return java BitSet object
     */
    public static BitSet hex2BitSet(byte[] b, int offset, boolean bitZeroMeansExtended) {
        int len = bitZeroMeansExtended
                ? ((Character.digit((char) b[offset], 16) & 0x08) == 8 ? 128 : 64)
                : 64;
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) b[offset + (i >> 2)], 16);
            if ((digit & (0x08 >> (i % 4))) > 0) {
                bmap.set(i + 1);
            }
        }
        return bmap;
    }

    /**
     * Converts an ASCII representation of a Bitmap field into a Java BitSet
     *
     * @param b - hex representation
     * @param offset - starting offset
     * @param maxBits - max number of bits (supports 8, 16, 24, 32, 48, 52,
     * 64,.. 128 or 192)
     * @return java BitSet object
     */
    public static BitSet hex2BitSet(byte[] b, int offset, int maxBits) {
        int len = maxBits > 64
                ? ((Character.digit((char) b[offset], 16) & 0x08) == 8 ? 128 : 64)
                : maxBits;
        if (len > 64 && maxBits > 128
                && b.length > offset + 16
                && (Character.digit((char) b[offset + 16], 16) & 0x08) == 8) {
            len = 192;
        }
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) b[offset + (i >> 2)], 16);
            if ((digit & (0x08 >> (i % 4))) > 0) {
                bmap.set(i + 1);
                if (i == 65 && maxBits > 128) {
                    len = 192;
                }
            }
        }
        return bmap;
    }

    /**
     * Converts an ASCII representation of a Bitmap field into a Java BitSet
     *
     * @param bmap - BitSet
     * @param b - hex representation
     * @param bitOffset - (i.e. 0 for primary bitmap, 64 for secondary)
     * @return java BitSet object
     */
    public static BitSet hex2BitSet(BitSet bmap, byte[] b, int bitOffset) {
        int len = b.length << 2;
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) b[i >> 2], 16);
            if ((digit & (0x08 >> (i % 4))) > 0) {
                bmap.set(bitOffset + i + 1);
            }
        }
        return bmap;
    }

    /**
     * @param b source byte array
     * @param offset starting offset
     * @param len number of bytes in destination (processes len*2)
     * @return byte[len]
     */
    public static byte[] hex2byte(byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i = 0; i < len * 2; i++) {
            int shift = i % 2 == 1 ? 0 : 4;
            d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
        }
        return d;
    }

    /**
     * Converts a hex string into a byte array
     *
     * @param s source string (with Hex representation)
     * @return byte array
     */
    public static byte[] hex2byte(String s) {
        if (s.length() % 2 == 0) {
            return hex2byte(s.getBytes(), 0, s.length() >> 1);
        } else {
            // Padding left zero to make it even size #Bug raised by tommy
            return hex2byte("0" + s);
        }
    }

    /**
     * Converts a byte array into a hex string
     *
     * @param bs source byte array
     * @return hexadecimal representation of bytes
     */
    public static String byte2hex(byte[] bs) {
        return byte2hex(bs, 0, bs.length);
    }

    /**
     * Converts an integer into a byte array of hex
     *
     * @param value
     * @return bytes representation of integer
     */
    public static byte[] int2byte(int value) {
        if (value < 0) {
            return new byte[]{(byte) (value >>> 24 & 0xFF), (byte) ((value >>> 16) & 0xFF),
                (byte) ((value >>> 8) & 0xFF), (byte) ((value) & 0xFF)};
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

//    public static void main(String[] args){
//        int a = 1;
//        int b = 10000;
//        int c = 666666666;
//        System.err.println(int2byte(a).length);
//        System.err.println(int2byte(b).length);
//        System.err.println(int2byte(c).length);
//        
//        System.err.println(byte2int(int2byte(a)));
//        System.err.println(byte2int(int2byte(b)));
//        System.err.println(byte2int(int2byte(c)));
//    }
    /**
     * Converts a byte array of hex into an integer
     *
     * @param bytes
     * @return integer representation of bytes
     */
    public static int byte2int(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
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
     * Converts a byte array into a string of lower case hex chars.
     *
     * @param bs A byte array
     * @param off The index of the first byte to read
     * @param length The number of bytes to read.
     * @return the string of hex chars.
     */
    public static String byte2hex(byte[] bs, int off, int length) {
        if (bs.length <= off || bs.length < off + length) {
            throw new IllegalArgumentException();
        }
        StringBuilder sb = new StringBuilder(length * 2);
        byte2hexAppend(bs, off, length, sb);
        return sb.toString();
    }

    private static void byte2hexAppend(byte[] bs, int off, int length, StringBuilder sb) {
        if (bs.length <= off || bs.length < off + length) {
            throw new IllegalArgumentException();
        }
        sb.ensureCapacity(sb.length() + length * 2);
        for (int i = off; i < off + length; i++) {
            sb.append(Character.forDigit((bs[i] >>> 4) & 0xf, 16));
            sb.append(Character.forDigit(bs[i] & 0xf, 16));
        }
    }

    /**
     * XML normalizer
     *
     * @param s source String
     * @param canonical true if we want to normalize \r and \n as well
     * @return normalized string suitable for XML Output
     */
    public static String normalize(String s, boolean canonical) {
        StringBuilder str = new StringBuilder();

        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '<':
                    str.append("&lt;");
                    break;
                case '>':
                    str.append("&gt;");
                    break;
                case '&':
                    str.append("&amp;");
                    break;
                case '"':
                    str.append("&quot;");
                    break;
                case '\r':
                case '\n':
                    if (canonical) {
                        str.append("&#");
                        str.append(Integer.toString(ch & 0xFF));
                        str.append(';');
                        break;
                    }
                // else, default append char
                default:
                    if (ch < 0x20) {
                        str.append("&#");
                        str.append(Integer.toString(ch & 0xFF));
                        str.append(';');
                    } else if (ch > 0xff00) {
                        str.append((char) (ch & 0xFF));
                    } else {
                        str.append(ch);
                    }
            }
        }
        return (str.toString());
    }

    /**
     * XML normalizer (default canonical)
     *
     * @param s source String
     * @return normalized string suitable for XML Output
     */
    public static String normalize(String s) {
        return normalize(s, true);
    }

    public static int[] toIntArray(String s) {
        StringTokenizer st = new StringTokenizer(s);
        int[] array = new int[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            array[i] = Integer.parseInt(st.nextToken());
        }
        return array;
    }

    public static String[] toStringArray(String s) {
        StringTokenizer st = new StringTokenizer(s);
        String[] array = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            array[i] = st.nextToken();
        }
        return array;
    }

    /**
     * Bitwise XOR between corresponding bytes
     *
     * @param op1 byteArray1
     * @param op2 byteArray2
     * @return an array of length = the smallest between op1 and op2
     */
    public static byte[] xor(byte[] op1, byte[] op2) {
        byte[] result;
        // Use the smallest array
        if (op2.length > op1.length) {
            result = new byte[op1.length];
        } else {
            result = new byte[op2.length];
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (op1[i] ^ op2[i]);
        }
        return result;
    }

    /**
     * Bitwise XOR between corresponding byte arrays represented in hex
     *
     * @param op1 hexstring 1
     * @param op2 hexstring 2
     * @return an array of length = the smallest between op1 and op2
     */
    public static String hexor(String op1, String op2) {
        byte[] xor = xor(hex2byte(op1), hex2byte(op2));
        return hexString(xor);
    }

    /**
     * Trims a byte[] to a certain length
     *
     * @param array the byte[] to be trimmed
     * @param length the wanted length
     * @return the trimmed byte[]
     */
    public static byte[] trim(byte[] array, int length) {
        byte[] trimmedArray = new byte[length];
        System.arraycopy(array, 0, trimmedArray, 0, length);
        return trimmedArray;
    }

    /**
     * Concatenates two byte arrays (array1 and array2)
     *
     * @param array1 first part
     * @param array2 last part
     * @return the concatenated array
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] concatArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatArray, 0, array1.length);
        System.arraycopy(array2, 0, concatArray, array1.length, array2.length);
        return concatArray;
    }
    
    /**
     * Concatenates two byte arrays (array1 and array2)
     *
     * @param array1 first part
     * @param beginIndex1 initial index
     * @param length1 length
     * @param array2 last part
     * @param beginIndex2 last part index
     * @param length2 last part length
     * @return the concatenated array
     */
    public static byte[] concat(byte[] array1, int beginIndex1, int length1, byte[] array2,
            int beginIndex2, int length2) {
        byte[] concatArray = new byte[length1 + length2];
        System.arraycopy(array1, beginIndex1, concatArray, 0, length1);
        System.arraycopy(array2, beginIndex2, concatArray, length1, length2);
        return concatArray;
    }

    /**
     * Causes the currently executing thread to sleep (temporarily cease
     * execution) for the specified number of milliseconds. The thread does not
     * lose ownership of any monitors.
     *
     * This is the same as Thread.sleep () without throwing InterruptedException
     *
     * @param millis the length of time to sleep in milliseconds.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Left unPad with '0'
     *
     * @param s - original string
     * @return zero unPadded string
     */
    public static String zeroUnPad(String s) {
        return unPadLeft(s, '0');
    }

    /**
     * Right unPad with ' '
     *
     * @param s - original string
     * @return blank unPadded string
     */
    public static String blankUnPad(String s) {
        return unPadRight(s, ' ');
    }

    /**
     * Unpad from right.
     *
     * @param s - original string
     * @param c - padding char
     * @return unPadded string.
     */
    public static String unPadRight(String s, char c) {
        int end = s.length();
        if (end == 0) {
            return s;
        }
        while ((0 < end) && (s.charAt(end - 1) == c)) {
            end--;
        }
        return (0 < end) ? s.substring(0, end) : s.substring(0, 1);
    }

    /**
     * Unpad from left.
     *
     * @param s - original string
     * @param c - padding char
     * @return unPadded string.
     */
    public static String unPadLeft(String s, char c) {
        int fill = 0, end = s.length();
        if (end == 0) {
            return s;
        }
        while ((fill < end) && (s.charAt(fill) == c)) {
            fill++;
        }
        return (fill < end) ? s.substring(fill, end) : s.substring(fill - 1, end);
    }

    /**
     * @return true if the string is zero-filled ( 0 char filled )
     *
     */
    public static boolean isZero(String s) {
        int i = 0, len = s.length();
        while (i < len && (s.charAt(i) == '0')) {
            i++;
        }
        return (i >= len);
    }

    /**
     * @return true if the string is blank filled (space char filled)
     */
    public static boolean isBlank(String s) {
        return (s.trim().length() == 0);
    }

    /**
     * Return true if the string is alphanum.
     * <code>{letter digit (.) (_) (-) ( ) (?) }</code>
     *
     *
     */
    public static boolean isAlphaNumeric(String s) {
        int i = 0, len = s.length();
        while (i < len && (Character.isLetterOrDigit(s.charAt(i))
                || s.charAt(i) == ' ' || s.charAt(i) == '.'
                || s.charAt(i) == '-' || s.charAt(i) == '_')
                || s.charAt(i) == '?') {
            i++;
        }
        return (i >= len);
    }

    /**
     * Return true if the string represent a number in the specified radix.
     * <br><br>
     *
     */
    public static boolean isNumeric(String s, int radix) {
        int i = 0, len = s.length();
        while (i < len && Character.digit(s.charAt(i), radix) > -1) {
            i++;
        }
        return (i >= len && len > 0);
    }

    /**
     * Converts a BitSet into an extended binary field used in pack routines.
     * The result is always in the extended format: (16 bytes of length)
     * <br><br>
     *
     * @param b the BitSet
     * @return binary representation
     */
    public static byte[] bitSet2extendedByte(BitSet b) {
        int len = 128;
        byte[] d = new byte[len >> 3];
        for (int i = 0; i < len; i++) {
            if (b.get(i + 1)) {
                d[i >> 3] |= (0x80 >> (i % 8));
            }
        }
        d[0] |= 0x80;
        return d;
    }

    /**
     * Converts a String to an integer of base radix.
     * <br><br>
     * String constraints are:
     * <li>Number must be less than 10 digits</li>
     * <li>Number must be positive</li>
     *
     * @param s String representation of number
     * @param radix Number base to use
     * @return integer value of number
     * @throws NumberFormatException
     */
    public static int parseInt(String s, int radix) throws NumberFormatException {
        int length = s.length();
        if (length > 9) {
            throw new NumberFormatException("Number can have maximum 9 digits");
        }
        int result;
        int index = 0;
        int digit = Character.digit(s.charAt(index++), radix);
        if (digit == -1) {
            throw new NumberFormatException("String contains non-digit");
        }
        result = digit;
        while (index < length) {
            result *= radix;
            digit = Character.digit(s.charAt(index++), radix);
            if (digit == -1) {
                throw new NumberFormatException("String contains non-digit");
            }
            result += digit;
        }
        return result;
    }

    /**
     * Converts a String to an integer of radix 10.
     * <br><br>
     * String constraints are:
     * <li>Number must be less than 10 digits</li>
     * <li>Number must be positive</li>
     *
     * @param s String representation of number
     * @return integer value of number
     * @throws NumberFormatException
     */
    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s, 10);
    }

    /**
     * Converts a character array to an integer of base radix.
     * <br><br>
     * Array constraints are:
     * <li>Number must be less than 10 digits</li>
     * <li>Number must be positive</li>
     *
     * @param cArray Character Array representation of number
     * @param radix Number base to use
     * @return integer value of number
     * @throws NumberFormatException
     */
    public static int parseInt(char[] cArray, int radix) throws NumberFormatException {
        int length = cArray.length;
        if (length > 9) {
            throw new NumberFormatException("Number can have maximum 9 digits");
        }
        int result;
        int index = 0;
        int digit = Character.digit(cArray[index++], radix);
        if (digit == -1) {
            throw new NumberFormatException("Char array contains non-digit");
        }
        result = digit;
        while (index < length) {
            result *= radix;
            digit = Character.digit(cArray[index++], radix);
            if (digit == -1) {
                throw new NumberFormatException("Char array contains non-digit");
            }
            result += digit;
        }
        return result;
    }

    /**
     * Converts a character array to an integer of radix 10.
     * <br><br>
     * Array constraints are:
     * <li>Number must be less than 10 digits</li>
     * <li>Number must be positive</li>
     *
     * @param cArray Character Array representation of number
     * @return integer value of number
     * @throws NumberFormatException
     */
    public static int parseInt(char[] cArray) throws NumberFormatException {
        return parseInt(cArray, 10);
    }

    /**
     * Converts a byte array to an integer of base radix.
     * <br><br>
     * Array constraints are:
     * <li>Number must be less than 10 digits</li>
     * <li>Number must be positive</li>
     *
     * @param bArray Byte Array representation of number
     * @param radix Number base to use
     * @return integer value of number
     * @throws NumberFormatException
     */
    public static int parseInt(byte[] bArray, int radix) throws NumberFormatException {
        int length = bArray.length;
        if (length > 9) {
            throw new NumberFormatException("Number can have maximum 9 digits");
        }
        int result;
        int index = 0;
        int digit = Character.digit((char) bArray[index++], radix);
        if (digit == -1) {
            throw new NumberFormatException("Byte array contains non-digit");
        }
        result = digit;
        while (index < length) {
            result *= radix;
            digit = Character.digit((char) bArray[index++], radix);
            if (digit == -1) {
                throw new NumberFormatException("Byte array contains non-digit");
            }
            result += digit;
        }
        return result;
    }

    /**
     * pads a string with 'F's (useful for pinoffset management)
     *
     * @param s an [hex]string
     * @param len desired length
     * @return string right padded with 'F's
     */
    public static String strpadf(String s, int len) {
        StringBuilder d = new StringBuilder(s);
        while (d.length() < len) {
            d.append('F');
        }
        return d.toString();
    }

    /**
     * reverse the effect of strpadf
     *
     * @param s F padded string
     * @return trimmed string
     */
    public static String trimf(String s) {
        if (s != null) {
            int l = s.length();
            if (l > 0) {
                while (--l >= 0) {
                    if (s.charAt(l) != 'F') {
                        break;
                    }
                }
                s = l == 0 ? "" : s.substring(0, l + 1);
            }
        }
        return s;
    }

    /**
     * Parse currency amount conversion rate string
     * <p/>
     * Suitble for parse fields 10 and 11
     *
     * @param convRate amount conversation rate
     * @return parsed currency amount conversation rate
     * @throws IllegalArgumentException
     */
    public static double parseAmountConversionRate(String convRate) {
        if (convRate == null || convRate.length() != 8) {
            throw new IllegalArgumentException("Invalid amount converion rate argument: '"
                    + convRate + "'");
        }
        BigDecimal bd = new BigDecimal(convRate);
        int pow = bd.movePointLeft(7).intValue();
        bd = new BigDecimal(convRate.substring(1));
        return bd.movePointLeft(pow).doubleValue();
    }

    /**
     * Converts a string[] into a comma-delimited String.
     *
     * Takes care of escaping commas using a backlash
     *
     * @see org.jpos.iso.ISOUtil#commaDecode(String)
     * @param ss string array to be comma encoded
     * @return comma encoded string
     */
    public static String commaEncode(String[] ss) {
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            if (s != null) {
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    switch (c) {
                        case '\\':
                        case ',':
                            sb.append('\\');
                            break;
                    }
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Decodes a comma encoded String as encoded by commaEncode
     *
     * @see org.jpos.iso.ISOUtil#commaEncode(String[])
     * @param s the command encoded String
     * @return String[]
     */
    public static String[] commaDecode(String s) {
        List<String> l = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!escaped) {
                switch (c) {
                    case '\\':
                        escaped = true;
                        continue;
                    case ',':
                        l.add(sb.toString());
                        sb = new StringBuilder();
                        continue;
                }
            }
            sb.append(c);
            escaped = false;
        }
        if (sb.length() > 0) {
            l.add(sb.toString());
        }
        return l.toArray(new String[l.size()]);
    }

    /**
     * Decodes a comma encoded String returning element in position i
     *
     * @param s comma encoded string
     * @param i position (starts at 0)
     * @return element in position i of comma encoded string, or null
     */
    public static String commaDecode(String s, int i) {
        String[] ss = commaDecode(s);
        int l = ss.length;
        return i >= 0 && i < l ? ss[i] : null;
    }

    /**
     * Compute card's check digit (LUHN)
     *
     * @param p PAN (without checkdigit)
     * @return the checkdigit
     */
    public static char calcLUHN(String p) {
        int i, crc;
        int odd = p.length() % 2;

        for (i = crc = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Invalid PAN " + p);
            }
            c = (char) (c - '0');
            if (i % 2 != odd) {
                crc += (c * 2) >= 10 ? ((c * 2) - 9) : (c * 2);
            } else {
                crc += c;
            }
        }

        return (char) ((crc % 10 == 0 ? 0 : (10 - crc % 10)) + '0');
    }

    public static String getRandomDigits(Random r, int l, int radix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l; i++) {
            sb.append(r.nextInt(radix));
        }
        return sb.toString();
    }
}
