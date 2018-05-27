package com.github.hellorpc.handler;

import com.georgeinfo.ginkgo.dynamic.ScannerException;
import com.georgeinfo.ginkgo.injection.annotation.Service;
import com.georgeinfo.ginkgo.injection.bean.BeanScope;
import com.georgeinfo.ginkgo.injection.context.impl.DefaultApplicationContextImpl;
import com.georgeinfo.ginkgo.injection.exception.DIException;
import com.georgeinfo.ginkgo.injection.handler.ClassScanningHandler;
import com.georgeinfo.ginkgo.injection.util.DIBasicUtil;
import com.github.hellorpc.annotation.ClientRootConfiguration;
import com.github.hellorpc.annotation.ServerRootConfiguration;
import com.github.hellorpc.configuration.ClientConfiguration;
import com.github.hellorpc.configuration.ServerRPConfiguration;
import com.github.hellorpc.def.HelloConstants;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.Set;

/**
 * @author George (GeorgeWorld@qq.com)
 */
public class HelloClassScanningHandler implements ClassScanningHandler {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(HelloClassScanningHandler.class);

    @Override
    public boolean scanningAndProcessing(DefaultApplicationContextImpl context, Set<String> classpathFileSet) throws ScannerException, DIException {
        //处理扫描到的注解标注类（默认只处理@Service一种注解）
        if (classpathFileSet == null || classpathFileSet.isEmpty()) {
            return false;
        }

        //循环处理所有扫描得到的类
        //因为在一个循环中，同时处理解析多种注解，所以，不能遇到一个不合法的配置类，就中断整个循环过程，
        //也许在接下来的循环中，会找到其他合法的配置类，所以，使用一个list来保存找到的合法配置类，最终循环
        //完整个类列表后，合法配置类List还是空的话，就说明最终没有找到合法的配置实现类
        LinkedList<ServerRPConfiguration> serverConfigList = new LinkedList<ServerRPConfiguration>();
        LinkedList<ClientConfiguration> clientConfigList = new LinkedList<ClientConfiguration>();
        for (String fileClasspath : classpathFileSet) {
            //尝试实例化扫描得到的类的对象
            Class<?> clazz = null;
            fileClasspath = StringUtils.removeEnd(fileClasspath.replace("/", "."), ".class");
            try {
                clazz = Class.forName(fileClasspath);
            } catch (ClassNotFoundException ex) {
                throw new ScannerException("### Wrong class path:[" + fileClasspath + "] when default handler processing.", ex);
            }

            //判断扫描到的类，是否标注了系列注解
            if (clazz.isAnnotationPresent(ServerRootConfiguration.class)) {//对于Hello Server 服务端配置实现类的处理
                //判断这个类，是否是RPConfiguration的子类
                if (ServerRPConfiguration.class.isAssignableFrom(clazz)) {
                    //找到了合法的RPC配置类
                    try {
                        ServerRPConfiguration rpConfiguration = (ServerRPConfiguration) clazz.newInstance();
                        serverConfigList.add(rpConfiguration);
                    } catch (IllegalAccessException | InstantiationException ex) {
                        LOG.error("## Exception when create ServerRPConfiguration instance.", ex);
                    }
                } else {
                    LOG.error("### " + clazz.getName() + " marked by @ServerRootConfiguration, but " +
                            "it is not instance of ServerRPConfiguration");
                }
            } else if (clazz.isAnnotationPresent(ClientRootConfiguration.class)) {//对于Hello RPC Client 客户端配置实现类的处理
                //判断这个类，是否是ClientConfiguration的子类
                if (ClientConfiguration.class.isAssignableFrom(clazz)) {
                    //找到了合法的RPC配置类
                    try {
                        ClientConfiguration clientConfiguration = (ClientConfiguration) clazz.newInstance();
                        clientConfigList.add(clientConfiguration);
                    } catch (IllegalAccessException | InstantiationException ex) {
                        LOG.error("## Exception when create ClientConfiguration instance.", ex);
                    }
                } else {
                    LOG.error("### " + clazz.getName() + " marked by @ClientRootConfiguration, but " +
                            "it is not instance of ClientConfiguration");
                }
            } else if (clazz.isAnnotationPresent(Service.class)) {//对Hello Server 服务提供类进行处理
                Service serviceAnnotation = clazz.getAnnotation(Service.class);
                String beanId = serviceAnnotation.name();
                if (beanId == null || beanId.trim().isEmpty()) {
                    //将类短名字转换为驼峰字符串
                    beanId = DIBasicUtil.classNameToBeanName(clazz);
                }

                context.addBean(beanId, clazz, serviceAnnotation.beanSope());
            }
        }

        //注册服务端配置类实例
        if (serverConfigList.isEmpty()) {
            throw new ScannerException("### Can't found Server configuration class in classpath.");
        } else {
            //获取最后一个读取到的配置类
            ServerRPConfiguration rpConfiguration = serverConfigList.pop();

            //向DI容器中注册RPC配置类实例
            context.addBean(HelloConstants.SERVER_CONFIG_OBJECT_KEY, rpConfiguration, BeanScope.singleton);
        }

        //注册客户端配置类实例
        if (clientConfigList.isEmpty()) {
            throw new ScannerException("### Can't found Client configuration class in classpath.");
        } else {
            //获取最后一个读取到的配置类
            ClientConfiguration clientConfiguration = clientConfigList.pop();

            //向DI容器中注册RPC配置类实例
            context.addBean(HelloConstants.CLIENT_CONFIG_OBJECT_KEY, clientConfiguration, BeanScope.singleton);
        }

        return true;
    }
}
