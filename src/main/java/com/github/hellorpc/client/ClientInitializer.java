package com.github.hellorpc.client;

import com.georgeinfo.ginkgo.injection.context.ApplicationContext;

/**
 * 客户端初始化器
 *
 * @author George (GeorgeWorld@qq.com)
 */
public interface ClientInitializer {
    public void doInit(ApplicationContext context) throws Exception;

}
