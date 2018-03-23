package com.github.hellorpc.protocol.check;

import com.github.hellorpc.client.ClientConfig;
import com.github.hellorpc.protocol.entities.HelloMsg;

public class ClientCheck {
    
    public static ReturnValue check(HelloMsg msg){
        ReturnValue rv = new ReturnValue();
        {//tpdu验证
            String tpdu = msg.getTpdu().getValue();
            if(tpdu.length()!=10){
                return rv.setRv(false).setMsg("### server tpdu length error , Length must be a 10, but only "+tpdu.length());
            }
            String prefix = tpdu.substring(0,2);
            if(!"60".equals(prefix)){
                return rv.setRv(false).setMsg("### server tpdu prefix error , the prefix is "+prefix+" not 60 .");
            }
            String from = tpdu.substring(2,6);
            if(!from.equals(ClientConfig.SERVER_TPDU)){ //不正确的服务端
                return rv.setRv(false).setMsg("### server tpdu from error , the server("+from+") is not "+ClientConfig.SERVER_TPDU);
            }
            String to = tpdu.substring(6,10);
            if(!ClientConfig.CLIENT_TPDU.equals(to)){//客户端请求目标是不这里
                return rv.setRv(false).setMsg("### server tpdu to error , the msg to "+to+" , not to me.");
            }
        }
        {//时间戳5分钟以内受理
            boolean b = msg.getBitmap().getBitSwitchArray()[0];
            if(b){
                long timestamp = msg.getDefinitedField().getTimestamp();
                if(System.currentTimeMillis()-timestamp > 1000*60*5){//大于5分钟,此请求不在受理
                    return rv.setRv(false).setMsg("### server reqponse timeout , is no longer accepted .");
                }
            }
        }
        {//mac校验
            boolean b = msg.getBitmap().getBitSwitchArray()[7];
            if(b){
                byte[] mac = msg.getDefinitedField().getMac();
                
            }
        }
        
        return rv;
    } 
    
}
