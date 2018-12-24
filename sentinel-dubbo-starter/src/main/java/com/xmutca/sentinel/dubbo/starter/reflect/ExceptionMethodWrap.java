package com.xmutca.sentinel.dubbo.starter.reflect;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * 方法包装
 */
@Getter
@Setter
public class ExceptionMethodWrap {

    /**
     * 方法
     */
    private Method method;

    /**
     * 实例
     */
    private Object instance;

    public ExceptionMethodWrap(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }
}