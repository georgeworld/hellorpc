package com.github.hellorpc.server;

import com.github.hellorpc.protocol.builder.MsgBuilder;
import com.github.hellorpc.protocol.check.ReturnValue;
import com.github.hellorpc.protocol.check.ServerCheck;
import com.github.hellorpc.protocol.entities.HelloMsg;
import com.github.hellorpc.protocol.parse.MsgParser;
import com.github.hellorpc.util.MsgPackException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
/**
 *
 * @author dev1
 */
public class VirtualServer extends Thread{
    
    private final Socket socket;

    @Override
    protected void finalize() throws Throwable {
        if(socket!=null){
            socket.close();
        }
    }
    public VirtualServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
    	System.out.println("run");
        try {
            while(true){
                if(!socket.isConnected()){
                    break;
                }
                InputStream is = socket.getInputStream();
                InputstreanValue iv = this.readHttpOrSocketStream(is);
                HelloMsg msg = null;
				try {
					msg = MsgParser.parseMsg(iv.getServerInputstream());
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				}//this.readDaufltStream(is)
                ReturnValue rv = ServerCheck.check(msg);
                try {
                    byte[] returnMsg = null;
                    if(!rv.isRv()){ //没有通过
                        returnMsg = packErrorReturn(rv.getMsg(), msg);
                    }else{
                        Class clazz = Class.forName(msg.getMsgContent().getPackagePath()+"."+msg.getMsgContent().getActionClassName());
                        Object obj = Registry.RegistryHolder.getInstance().getRegistObj(clazz);
                        Class[] clazzs = msg.getMsgContent().getMethodParamsTypeList().toArray(new Class[]{});
                        Object[] os = msg.getMsgContent().getMethodParamsList().toArray();
                        Method method = obj.getClass().getMethod(msg.getMsgContent().getMethodName(),clazzs);
                        Object rs = method.invoke(obj, msg.getMsgContent().getMethodParamsList().toArray());
                        returnMsg = packSuccessReturn(rs, msg);
                    }
                    OutputStream os = socket.getOutputStream();
                    if (iv.flag) {
                    	os.write("HTTP/1.1 200 OK\n".getBytes());
                    	os.write("Content-Type: text/html;charset=utf8\n".getBytes());
                    	os.write("\n".getBytes());
                    }
                    os.write(returnMsg);
//                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os)); 
//                    bufferedWriter.write("HTTP/1.1 200 OK\n");  
//                    bufferedWriter.write("Content-Type: text/html;charset=utf8\n");  
//                    bufferedWriter.write("\n"); // 区分HEAD区和正文区  
//                    bufferedWriter.write(new String(returnMsg));
//                    bufferedWriter.flush();
                    os.close();
                } catch (MsgPackException | IOException e) {
                    e.printStackTrace();
                }
                if(msg.getHeader().getDataRule().isConnkeepalive()){//长连接 跳转到下次循环的开始   否者退出
                    continue;
                }else{
                   // socket.close();
                    break;
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }catch(IOException e){
            try {
                socket.close();
                throw new RuntimeException("#####The other abnormal exit , err Msg :"+e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException("##### The other close error :"+e.getMessage());
            }
        }
        
    }
    
    public byte[] packSuccessReturn(Object rs,HelloMsg fromMsg)throws MsgPackException,IOException{
        String from =fromMsg.getTpdu().getValue().substring(2,6);
        String to = fromMsg.getTpdu().getValue().substring(6,10);
        MsgBuilder builder = MsgBuilder.begin().buildTpdu().addTpduFrom(to).addTpduTo(from).done()
                .buildTimestamp(fromMsg.getTimestamp())
                .buildMti().addMti(fromMsg.getMti()).done()
                .buildDefinitedFields().addDefinitedField0(System.currentTimeMillis()).done()
                .buildMsgContent().addReturnValue(rs).done()
                .buildHeader().addActionCode("00").addCompressionFlag(fromMsg.getHeader().getDataRule().isCompression())
                    .addEncryptionFlag(fromMsg.getHeader().getDataRule().isEncryption()).addConnkeepaliveFlag(fromMsg.getHeader().getDataRule().isConnkeepalive())
                .addExtendedDataSegment((byte)0,(byte) 0).addReleasedFlag(fromMsg.getHeader().getProtocolVersion().isReleased()).addVersion(fromMsg.getHeader().getProtocolVersion().getVersion()).done();
        return builder.pack();
    }
    
    public byte[] packErrorReturn(String errorMsg,HelloMsg fromMsg)throws MsgPackException,IOException{
        String from =fromMsg.getTpdu().getValue().substring(2,6);
        String to = fromMsg.getTpdu().getValue().substring(6,10);
        MsgBuilder builder = MsgBuilder.begin().buildTpdu().addTpduFrom(to).addTpduTo(from).done()
                .buildTimestamp(fromMsg.getTimestamp())
                .buildMti().addMti(fromMsg.getMti()).done()
                .buildDefinitedFields().addDefinitedField0(System.currentTimeMillis()).addDefinitedField1(errorMsg).done()
                .buildHeader().addActionCode("01").addCompressionFlag(fromMsg.getHeader().getDataRule().isCompression())
                    .addEncryptionFlag(fromMsg.getHeader().getDataRule().isEncryption()).addConnkeepaliveFlag(fromMsg.getHeader().getDataRule().isConnkeepalive())
                .addExtendedDataSegment((byte)0,(byte) 0).addReleasedFlag(fromMsg.getHeader().getProtocolVersion().isReleased()).addVersion(fromMsg.getHeader().getProtocolVersion().getVersion()).done();
        return builder.pack();
    }
    
public InputStream readDaufltStream(InputStream serverInputstream) {
		
		byte[] cbuf = new byte[3] ;
		try {
			int leng = serverInputstream.available() ;
			serverInputstream.read(cbuf) ;
			String three = new String(cbuf,0,cbuf.length).toUpperCase() ;
			//判断http/socket
			if ("POS".equals(three) || "GET".equals(three)) {//http
				cbuf = new byte[1] ;
				byte pre = 0 ;
				while (serverInputstream.read(cbuf) != -1) {
					//回车13 10 13 表示头结束
					if ( (cbuf[0]&0xff) == 13 ) {
						//如果前一个char为换行 则读httphead结束
						if ( (pre&0xff) == 10 ) {
							//跳过13
							serverInputstream.skip(1) ;
							break ;
						}
					}
					pre = cbuf[0] ;
				}
				leng = serverInputstream.available() ;
				cbuf = null ;
			}
			else {//socket
			}
			
			byte[] resbuf = new byte[leng];
			int index = 0; 
			if (cbuf!=null) {
				System.arraycopy(cbuf, 0, resbuf, 0, cbuf.length);
				index = cbuf.length; 
			}
			cbuf = new byte[1] ;
			while (serverInputstream.read(cbuf) != -1) {
				if(cbuf[0]==13) {
					continue ;
				}
				System.arraycopy(cbuf, 0, resbuf, index, cbuf.length);
				index += cbuf.length ;
				if (serverInputstream.available() == 0) {
					break ;
				}
			}
			serverInputstream = new ByteArrayInputStream(resbuf) ;
		} catch (IOException e) {
			System.out.println("读取报文流异常："+e);
		}
		return serverInputstream ;
	}

	public InputstreanValue readHttpOrSocketStream(InputStream serverInputstream) {
		InputstreanValue iv = new InputstreanValue() ;
		byte[] cbuf = new byte[3] ;
		try {
			//判断http/socket
			byte[] resbuf = new byte[serverInputstream.available()];
			serverInputstream.read(resbuf);
			String str = new String(resbuf,0,3);
			if ("POS".equals(str) || "GET".equals(str)) {
				byte[] newbuf = null ;
				for (int i = 0 ; i < resbuf.length-4 ; i++) {
					if (resbuf[i]==13&&resbuf[i+1]==10&&resbuf[i+2]==13&&resbuf[i+3]==10) {
						int newl = resbuf.length - (i+4)  ;
						newbuf = new byte[newl] ;
						System.arraycopy(resbuf, i+4, newbuf, 0, newl);
						break ;
					}
				}
				serverInputstream = new ByteArrayInputStream(newbuf) ;
				iv.setFlag(true);
			}
			else {
				serverInputstream = new ByteArrayInputStream(resbuf) ;
				iv.setFlag(false);
			}
			iv.setServerInputstream(serverInputstream);
		} catch (IOException e) {
			System.out.println("读取报文流异常："+e);
		}
		return iv ;
	}
}
