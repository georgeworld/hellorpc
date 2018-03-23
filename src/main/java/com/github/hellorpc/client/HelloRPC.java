package com.github.hellorpc.client;

import com.github.hellorpc.protocol.builder.MsgBuilder;
import com.github.hellorpc.protocol.check.ClientCheck;
import com.github.hellorpc.protocol.check.ReturnValue;
import com.github.hellorpc.protocol.entities.HelloMsg;
import com.github.hellorpc.protocol.parse.MsgParser;
import com.github.hellorpc.util.MsgPackException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author dev1
 */
public class HelloRPC {

    private final String host;
    private final int port;
    private final String uri;
    private final boolean bySocket;

    private Socket socket;

    /**
     * @param host
     * @param port
     * @param uri
     * @param bySocket 传输方式：socket=(true)，http=(false)
     */
    public HelloRPC(String host, int port, String uri, boolean bySocket) {
        this.host = host;
        this.port = port;
        this.uri = uri;
        this.bySocket = bySocket;
    }

    @Override
    protected void finalize() throws Throwable {
        if (socket != null && socket.isConnected()) {
            socket.close();
        }
    }


    public <T> T getClient(final Class<T> clazz) {
        Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //before 解析参数 拼装报文  
                ArrayList<Object> paramsList = null;
                if (args != null) {
                    paramsList = new ArrayList<>();
                    for (Object arg : args) {
                        paramsList.add(arg);
                    }
                }
                ArrayList<Class> paramsTypeList = null;
//                ArrayList<String> paramsTypeList = null;
                if (args != null) {
                    paramsTypeList = new ArrayList<>();
                    for (Class c : method.getParameterTypes()) {

                        paramsTypeList.add(c);
                    }
                }
                MsgBuilder builder = MsgBuilder.begin()
                        .buildTpdu()
                        .addTpduFrom(ClientConfig.CLIENT_TPDU)
                        .addTpduTo(ClientConfig.SERVER_TPDU)
                        .done()
                        .buildTimestamp(System.currentTimeMillis())
                        .buildMti()
                        .addMti("0000")
                        .done()
                        .buildBitMap()
                        .done()
                        .buildDefinitedFields().addDefinitedField0(System.currentTimeMillis()).done()
                        .buildMsgContent()
                        .addActionClassName(clazz.getSimpleName())
                        .addMethodName(method.getName())
                        .addPackagePath(clazz.getName().replace("." + clazz.getSimpleName(), ""))
                        .addMethodParamsList(paramsList)
                        .addMethodParamsTypeList(paramsTypeList)
                        .done()
                        .buildHeader()
                        .addVersion(Short.valueOf("002"))
                        .addCompressionFlag(ClientConfig.COMPRESSION)
                        .addEncryptionFlag(ClientConfig.ENCRYPTION)
                        .addActionCode("00")
                        .addExtendedDataSegment((byte) 0, (byte) 0)
                        .addReleasedFlag(false)
                        .addConnkeepaliveFlag(ClientConfig.CONN_KEEPALIVE)
                        .done();
                //middle 发送报文 接受报文
                byte[] reqData = builder.pack();
                InputStream ins = null;
                if (bySocket) {//socket
                    Socket s = getSocket();
                    s.getOutputStream().write(reqData);
                    ins = s.getInputStream();
                } else {
                    ClentConnect clentcon = new ClentConnect(host, port, uri);
                    HttpURLConnection conn = clentcon.setConn();
                    conn.getOutputStream().write(reqData);
                    ins = conn.getInputStream();
                }
//                byte[] bs = new byte[ins.available()];
//                ins.read(bs);
//                System.out.println(new String(bs));
                //after 解析报文  返回数据
                HelloMsg respMsg = MsgParser.parseMsg(ins);
                ReturnValue rv = ClientCheck.check(respMsg);
                if (!rv.isRv()) {
                    throw new MsgPackException(rv.getMsg());
                }
                if ("00".equals(respMsg.getHeader().getActionCode())) {
                    return respMsg.getMsgContent().getReturnValue();
                } else if ("0".equals(respMsg.getHeader().getActionCode())) {
                    return respMsg.getMsgContent().getReturnValue();
                } else {
                    throw new RuntimeException(respMsg.getDefinitedField().getErrMsg());
                }
            }
        });
        return (T) obj;
    }

    /**
     * 动态代理，执行一个接口方法时，通过这个方法得到socket对象
     * TODO: 现在是每执行一个接口方法，就创建一个新的socket对象，哪怕是同一个接口的不同方法，
     * 也是每次都创建新的socket对象，以后要设计一个socket执行容器，一个接口实例，对应一个socket对象。
     */
    private Socket getSocket() throws IOException {
//        if (socket == null) {
        socket = new Socket(host, port);
//        System.out.println("#######创建了新的Socket对象");
//        }else{
//            System.out.println("#######使用旧的Socket对象");
//        }
        if (!socket.isConnected()) {//连接不成功
            System.out.println("#######创建了新的Socket对象，但是无法连接上。");
            socket.close();
            socket = new Socket(host, port);
        }
        return socket;
    }


}
