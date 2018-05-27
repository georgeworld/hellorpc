package com.github.hellorpc.container;

import com.georgeinfo.ginkgo.injection.context.impl.DefaultApplicationContextImpl;
import com.georgeinfo.ginkgo.injection.exception.DIException;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;

/**
 * @author George (GeorgeWorld@qq.com)
 */
public class Ioc {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(Ioc.class);

    /**
     * 根据接口类（或者Bean类本身）得到其实现类对象
     *
     * @param interfaceClass 所要查询的bean实现类所对应的接口类（或者bean实现类本身）
     * @return 查询出的bean实现类对象
     */
    public static <T> T getBeanByInterface(Class<T> interfaceClass) {
        try {
            T obj = DefaultApplicationContextImpl.getInstance().getBeanInstanceByInterfaceName(interfaceClass.getName());
            if (obj != null) {
                return obj;
            }
        } catch (DIException ex) {
            LOG.error("### Can't got instance of class/interface:" + interfaceClass);
        }

        return null;
    }

    /**
     * 根据bean ID查询bean实例
     *
     * @param beanId bean注册时所提供的ID，一般是短类名（短接口名）的驼峰字符串
     * @return 查询出的bean实现类对象
     **/
    public static <T> T getBeanById(String beanId) {
        try {
            return (T) DefaultApplicationContextImpl.getInstance().getBeanInstanceById(beanId);
        } catch (DIException ex) {
            LOG.error("### Can't got instance by bean id:" + beanId);
            return null;
        }
    }
}
