package com.github.hellorpc.protocol.check;

import com.github.hellorpc.protocol.entities.HelloMsg;
import com.github.hellorpc.server.ServerConfig;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dev1
 */
public class ServerCheck {
    
    
    public static ReturnValue check(HelloMsg msg){
        ReturnValue rv = new ReturnValue();
        {//tpdu验证
            String tpdu = msg.getTpdu().getValue();
            if(tpdu.length()!=10){
                return rv.setRv(false).setMsg("### tpdu length error , Length must be a 10, but only "+tpdu.length());
            }
            String prefix = tpdu.substring(0,2);
            if(!"60".equals(prefix)){
                return rv.setRv(false).setMsg("### tpdu prefix error , the prefix is "+prefix+" not 60 .");
            }
            String from = tpdu.substring(2,6);
            if(!StringUtils.containsAny(from, ServerConfig.AUTH_CLIENT_TPDU)){ //不在授权里面
                return rv.setRv(false).setMsg("### tpdu from error , the other("+from+") is not authorized");
            }
            String to = tpdu.substring(6,10);
            if(!ServerConfig.SERVER_TPDU.equals(to)){//客户端请求目标是不这里
                return rv.setRv(false).setMsg("### tpdu to error , the server is ("+ServerConfig.SERVER_TPDU+") but you to ("+to+").");
            }
        }
        {//时间戳5分钟以内受理
            long reqtime = msg.getTimestamp();
            long currenttime = System.currentTimeMillis();
            if(currenttime-reqtime > 1000*60*5){//大于5分钟,此请求不在受理
                return rv.setRv(false).setMsg("### Timestamp error , 5 minutes before request, is no longer accepted .");
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
