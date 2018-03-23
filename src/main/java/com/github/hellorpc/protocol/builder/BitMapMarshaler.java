package com.github.hellorpc.protocol.builder;

import com.github.hellorpc.protocol.entities.BitMap;
import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.DataFormatException;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class BitMapMarshaler {

    private final MsgBuilder msgBuilder;
    private BitMap bitMap;

    public BitMapMarshaler(MsgBuilder msgBuilder) {
        this.msgBuilder = msgBuilder;
        this.bitMap = new BitMap();
    }
    
    public BitMapMarshaler addBit(int fieldId, boolean isOpen) {
        if (fieldId >= 8) {
            throw new DataFormatException("## Field id can't bigger than 7,field id starts with 0.");
        }
        if (fieldId < 0) {
            throw new DataFormatException("## Field id can't less than 0.");
        }
        this.bitMap.getBitSwitchArray()[fieldId] = isOpen;

        return this;
    }

    public BitMapMarshaler setBitSwitchArray(Boolean[] ba) {
        if (ba.length != 8) {
            throw new DataFormatException("## Fields count must be 8");
        }
        this.bitMap.setBitSwitchArray(ba);

        return this;
    }

    public MsgBuilder done() {
        msgBuilder.getMsg().setBitmap(bitMap);
        msgBuilder.getDataMap().put(MsgType.BitMap, pack());
        return msgBuilder;
    }

    public byte[] pack() {
        return pack(this.bitMap);
    }

    public byte[] pack(BitMap bitMap) {
        double num = 0;
        for (int fieldId = 0; fieldId < 8; fieldId++) {
            if (bitMap.getBitSwitchArray()[fieldId] == true) {
                num = (num + Math.pow(2, fieldId));
            }
        }
        byte data = (byte) ((int) num & 0xFF);
        return new byte[]{data};
    }

    public BitMap unpack(byte[] ba) {
        if (ba == null) {
            throw new DataFormatException("## Data can't be null.");
        }
        if (ba.length != 1) {
            throw new DataFormatException("## Data length must be 1 byte.");
        }

        String binString = BinaryTool.toBinaryString(ba);
        char[] ca = binString.toCharArray();

        for (int i = 0; i < 8; i++) {
            this.bitMap.getBitSwitchArray()[i] = (ca[7 - i] == '1');
        }

        return this.bitMap;
    }

    public static void main(String[] args) {
//        byte[] data = MsgBuilder.begin().buildBitMap()
//                .addBit(0, true)
//                .addBit(1, true)
//                .addBit(2, true)
//                .addBit(3, true)
//                .addBit(4, false)
//                .addBit(5, false)
//                .addBit(6, false)
//                .addBit(7, false).pack();
//        System.out.println(BinaryTool.toBinaryString(data));
//
//        BitMap bm = MsgBuilder.begin().buildBitMap().unpack(data);
//        byte[] data2 = MsgBuilder.begin().buildBitMap().pack(bm);
//        System.out.println(BinaryTool.toBinaryString(data2));
    }
}
