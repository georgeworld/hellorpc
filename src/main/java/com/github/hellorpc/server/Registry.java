package com.github.hellorpc.server;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dev1
 */
public class Registry {

    private final Map<Class, Object> objs = new HashMap<>();

    public void regist(Object regObj, Class interfaceClass) {
        if (regObj != null) {
            if (objs.get(interfaceClass) != null) {
                throw new RuntimeException("This interface has been registered");
            }
            objs.put(interfaceClass, regObj);
        }
    }

    public <T> T getRegistObj(Class<T> interfaceClass) {
        return (T) objs.get(interfaceClass);
    }

    private Registry() {
    }

    public static class RegistryHolder {

        private static final Registry instance = new Registry();

        public static Registry getInstance() {
            return instance;
        }
    }

}
