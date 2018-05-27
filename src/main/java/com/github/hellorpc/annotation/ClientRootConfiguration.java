package com.github.hellorpc.annotation;

import java.lang.annotation.*;

/**
 * @author George (GeorgeWorld@qq.com)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClientRootConfiguration {
    /**
     * 配置类的名字，其实没什么用途，先占个位置，以后可能有用
     */
    public String name() default "";
}