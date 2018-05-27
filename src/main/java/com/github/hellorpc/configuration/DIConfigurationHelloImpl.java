package com.github.hellorpc.configuration;

import com.georgeinfo.ginkgo.injection.annotation.Configuration;
import com.georgeinfo.ginkgo.injection.config.DIConfiguration;
import com.georgeinfo.ginkgo.injection.handler.ClassScanningHandler;
import com.github.hellorpc.handler.HelloClassScanningHandler;

/**
 * Hello RPC提供的DI环境配置实现类
 *
 * @author George (GeorgeWorld@qq.com)
 */
@Configuration
public class DIConfigurationHelloImpl implements DIConfiguration {
    @Override
    public ClassScanningHandler getClassScanningHandler() {
        return new HelloClassScanningHandler();
    }
}
