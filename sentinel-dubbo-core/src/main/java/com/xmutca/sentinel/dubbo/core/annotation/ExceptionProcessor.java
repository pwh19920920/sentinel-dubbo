package com.xmutca.sentinel.dubbo.core.annotation;

import java.lang.annotation.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionProcessor {

    /**
     * 异常
     * @return
     */
    Class<? extends Throwable>[] value() default {};
}
