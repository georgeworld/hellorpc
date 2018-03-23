/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.tlv.bertlv;

import com.github.hellorpc.util.BinaryTool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * BER_TLV及EMV_TLV数据解析器（将byte[]解析成TLV对象）
 *
 * @author George <Georgeinfo@163.com>
 */
public abstract class TlvParser {

    public static void main(String[] args) throws DecoderException, UnsupportedEncodingException, IOException {
        //EMV_TLV测试：第一个tag的值：A000000333
        String tlvHex = "9F0605A0000003339F220101DF050420091231DF060101DF070101DF028180BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93BDF040103DF0314E881E390675D44C2DD81234DCE29C3F5AB2297A0";
        List<Tlv> tlvList = buildTlvList(Hex.decodeHex(tlvHex.toCharArray()));
        for (Tlv tlv : tlvList) {
            System.out.println("TAG:[" + Integer.toHexString(tlv.getTag().getEmvTag()) + "],value:[" + Hex.encodeHexString(tlv.getValue()) + "]");
            System.out.println("----------------------------------------------------------------");
        }

        System.out.println("*****************************************************************");

        //BER_TLV测试：第一个tag的值：com,对应的原始byte[]是：636f6d，换成BER_TLV后是：
        String tlvHex2 = "9Fe3ef6d05A000000333" ;// "9F e3 ef 6d 05 A0 00 00 03 33";
        List<Tlv> tlvList2 = buildTlvList(Hex.decodeHex(tlvHex2.toCharArray()));
        for (Tlv tlv : tlvList2) {
            System.out.println("TAG:[" + tlv.getTag().getBerTag() + "],value:[" + Hex.encodeHexString(tlv.getValue()) + "]");
            System.out.println("*****************************************************************");
        }
    }

    /**
     * 将16进制字符串转换为TLV对象列表
     *
     * @param data
     * @return
     */
    public static List<Tlv> buildTlvList(byte[] data) throws IOException {
        List<Tlv> tlvs = new ArrayList<Tlv>();

        int position = 0;
        while (position != data.length) {
            Tag tag = getTag(data, position);
            position += tag.getTagFullLength();

            VlengthPositon l_position = getLengthAndPosition(data, position);
            int valueLength = l_position.getValueLength();

            position = l_position.getDataPosition();
            byte[] _value = new byte[valueLength];
            System.arraycopy(data, position, _value, 0, valueLength);

            position = position + _value.length;
            tlvs.add(new Tlv(tag, valueLength, _value));
        }
        return tlvs;
    }

    /**
     * 将16进制字符串转换为TLV对象MAP
     *
     * @param data
     * @return
     */
    public static Map<Tag, Tlv> buildTlvMap(byte[] data) throws UnsupportedEncodingException, IOException {
        Map<Tag, Tlv> tlvs = new HashMap<Tag, Tlv>();

        int position = 0;
        while (position != data.length) {
            Tag tag = getTag(data, position);

            position += tag.getTagFullLength();

            VlengthPositon l_position = getLengthAndPosition(data, position);

            int _vl = l_position.getValueLength();
            position = l_position.getDataPosition();
            byte[] _value = new byte[_vl];
            System.arraycopy(data, position, _value, 0, _vl);
            position = position + _value.length;

            tlvs.put(tag, new Tlv(tag, _vl, _value));
        }
        return tlvs;
    }

    /**
     * 返回最后的Value的长度
     *
     * @param data
     * @param position
     * @return
     */
    private static VlengthPositon getLengthAndPosition(byte[] data, int position) {
        byte firstByteOfValueLength = data[position];
        int valueLength = 0;

        if (((firstByteOfValueLength >>> 7) & 0x01) == 0) {//如果第一个字节的bit8位是0
            valueLength = (firstByteOfValueLength & 0xFF);
            position = position + 1;
        } else {
            // 当value长度段第一个字节的最左侧的bit位为1的时候，取得后7bit的值，
            int valueLengthLength = (firstByteOfValueLength & 0x7F); //0X7F = 127 = 0b01111111;
            position = position + 1;
            byte[] vLL = new byte[valueLengthLength];
            System.arraycopy(data, position, vLL, 0, valueLengthLength);
            valueLength = BinaryTool.byte2int(vLL);
            position = position + valueLengthLength;
        }

        return new VlengthPositon(valueLength, position);
    }

    /**
     * 取得子域Tag标签
     *
     * @param data
     * @param position
     * @return
     */
    private static Tag getTag(byte[] data, int position) throws UnsupportedEncodingException, IOException {
        byte firstByte = data[position];
        int i = firstByte;
        if ((i & 0x1f) < 0x1f) { //0x1f = 0b00011111，Tag用第一个字节的后5位足以表示
            return new Tag(new byte[]{firstByte}, firstByte);
        } else {//如果第一个字节的bit5-bit1是11111，则从第二个字节开始，循环遍历
            {
                if ((data[position + 1] & 0xFF) <= 0x7F) {//0x7F = 0b01111111 ，只需要两个字节即可表示tag
                    //将第一字节和第二字节解析为EMV_TLV格式的tag
                    byte[] tagData = new byte[]{data[position], data[position + 1]};
                    return new Tag(tagData, BinaryTool.byte2int(tagData));
                } else {
                    byte[] berTagLabel;
                    //从第二个字节开始，循环拼接获取tag的值
                    try ( //解析为BER_TLV格式的tag
                            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        //从第二个字节开始，循环拼接获取tag的值
                        int index = position + 1;
                        for (; index < data.length; index++) {
                            if ((data[index] & 0xFF) <= 0x7F) {
                                baos.write(new byte[]{data[index]});
                                break;
                            } else {
                                baos.write(new byte[]{
                                    (byte) ((data[index] - 0x80) & 0xFF) //0x80 = 0b10000000
                                });
                            }
                        }
                        berTagLabel = baos.toByteArray(); // new byte[index + 1];
                    }

                    //构建整个Tag的数据
                    byte[] berTag = new byte[berTagLabel.length + 1];
                    //把第一个字节(类型标识所在字节)放入字节流中
                    berTag[0] = firstByte;
                    System.arraycopy(berTagLabel, 0, berTag, 1, berTagLabel.length);
                    return new Tag(berTag, new String(berTagLabel, "UTF-8"));
                }
            }
        }
    }
}
