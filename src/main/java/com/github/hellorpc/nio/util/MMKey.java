package com.github.hellorpc.nio.util;

import com.georgeinfo.basic.toolkit.MultiKeyMapKey;

/**
 * @author George (GeorgeWorld@qq.com)
 */
public class MMKey extends MultiKeyMapKey {

    public MMKey(Integer key) {
        super.addKey(key);
    }

    public MMKey(String key) {
        super.addKey(key);
    }
}
