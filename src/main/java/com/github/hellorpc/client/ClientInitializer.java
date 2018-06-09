package com.github.hellorpc.client;

import com.georgeinfo.ginkgo.injection.context.ApplicationContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;

/**
 * 客户端初始化器
 *
 * @author George (GeorgeWorld@qq.com)
 */
public interface ClientInitializer {
    public Bootstrap getBootstrap();

    public void doInit() throws Exception;

    public void doInit(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) throws Exception;

    public ApplicationContext getApplicationContext();
}
