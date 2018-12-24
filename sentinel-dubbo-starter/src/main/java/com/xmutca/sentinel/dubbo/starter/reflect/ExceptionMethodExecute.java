package com.xmutca.sentinel.dubbo.starter.reflect;

import com.xmutca.sentinel.dubbo.core.result.Result;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-24
 */
public class ExceptionMethodExecute {

    private ExceptionMethodExecute() {

    }

    /**
     * 异常处理
     * @param throwable
     * @return
     */
    public static Object handlerException(Throwable throwable) {
        ExceptionMethodWrap exceptionMethodWrap = ExceptionRegistry.lookup(throwable.getClass());
        if (null == exceptionMethodWrap) {
            return Result.DEFAULT_ERROR_RESULT;
        }

        Method method = exceptionMethodWrap.getMethod();
        method.setAccessible(true);
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        Object instance = isStatic ? null : exceptionMethodWrap.getInstance();
        Object result = null;
        try {
            result = method.invoke(instance, throwable);
        } catch (Exception ex) {
            return Result.DEFAULT_ERROR_RESULT;
        }

        if (null == result) {
            return Result.DEFAULT_ERROR_RESULT;
        }
        return result;
    }
}
