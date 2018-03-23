package com.github.hellorpc.server;

/**
 * RPC服务端配置
 */
public class ServerConfig {
    /**
     * 服务端tpdu
     */
    public static final String SERVER_TPDU = "0001";
    /**
     * 服务端授权的客户端tpdu
     */
    public static final String[] AUTH_CLIENT_TPDU = new String[]{"0010", "0020", "0030", "0040", "0050", "0060"};
    /**
     * 报文有效期
     */
    public static final long MSG_EXP_TIME = 6 * 60 * 1000l;

}
