package com.github.hellorpc.client;


/**
 * RPC客户端配置
 */
public class ClientConfig {
    /**服务端tpdu*/
    public static final String SERVER_TPDU = "0001";
    /**客户端tpdu*/
    public static final String CLIENT_TPDU = "0010";
    /**报文有效期*/
    public static final long MSG_EXP_TIME = 6 * 60 * 1000l;
    /**是否长连接*/
    public static final boolean CONN_KEEPALIVE = true;
    /**报文是否压缩*/
    public static final boolean COMPRESSION = false;
    /**报文是否加密*/
    public static final boolean ENCRYPTION = false;
    
    
}
