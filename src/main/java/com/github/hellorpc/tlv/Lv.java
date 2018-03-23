package com.github.hellorpc.tlv;

import com.github.hellorpc.util.BinaryTool;
import com.github.hellorpc.util.ByteArrayTool;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dev1
 */
public class Lv {
    
    
    public static byte[] data2Lv(byte[] data){
        byte byte1 = 0;
        int length = data.length;
        if(length > 0b01111111){ //数据长度大于127，则第一个byte来表示 数据长度的长度
            byte[] length_data = BinaryTool.int2byte(length);
            byte1 = (byte)(0b10000000 | length_data.length);
            return ByteArrayTool.concat(new byte[]{byte1},length_data,data);
        }else{
            byte1 = (byte)length;
            return ByteArrayTool.concat(new byte[]{byte1},data);
        }
    }
    /**
     * @param lvsData 多个lv拼接在一起的一个byte[]
     * @return  List[byte[]] 
     */
    public static List<byte[]> lvs2Datas(byte[] lvsData){
        try {
            List<byte[]> lvs = new ArrayList<>();
            try(ByteArrayInputStream is = new ByteArrayInputStream(lvsData)){
                while(true){
                    int b = is.read();
                    if(b == -1){break;}
                    if((b&0xff)>>7 == 1){//bit8是1,这个byte的数据是data_length_length
                        int data_length_length = b&0b01111111;
                        byte[] data_length_data = new byte[data_length_length];
                        is.read(data_length_data);
                        int data_length = BinaryTool.byte2int(data_length_data);
                        byte[] lv = new byte[data_length];
                        is.read(lv);
                        lvs.add(lv);
                    }else{//bit8是0，这个byte的数据就是data_length
                        int data_length = b&0xff;
                        byte[] lv = new byte[data_length];
                        is.read(lv);
                        lvs.add(lv);
                    }
                }
                return lvs;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
}
