package com.github.hellorpc.nio.client;

import com.github.hellorpc.client.HelloRPC;

import java.net.InetSocketAddress;

/**
 * Hello RPC Netty 客户端接口
 *
 * @author George (GeorgeWorld@qq.com)
 */
public interface HelloRPCNetty extends HelloRPC {
    public void setAddress(InetSocketAddress serverAddress);

    public InetSocketAddress getAddress();
}
