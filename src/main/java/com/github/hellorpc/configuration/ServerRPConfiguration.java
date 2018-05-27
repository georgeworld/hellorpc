package com.github.hellorpc.configuration;

import com.georgeinfo.ginkgo.injection.annotation.Configuration;
import com.georgeinfo.ginkgo.injection.handler.ClassScanningHandler;

/**
 * Hello RPC配置接口
 * @author George (GeorgeWorld@qq.com)
 */
public interface ServerRPConfiguration {
    public int getPort();
}
