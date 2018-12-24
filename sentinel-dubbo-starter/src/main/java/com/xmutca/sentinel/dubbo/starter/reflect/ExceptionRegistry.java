package com.xmutca.sentinel.dubbo.starter.reflect;

import com.xmutca.sentinel.dubbo.core.annotation.ExceptionProcessor;
import lombok.Getter;
import lombok.Setter;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册
 */
@Getter
@Setter
public final class ExceptionRegistry {

    private static final Map<String, ExceptionMethodWrap> EXCEPTION_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Object> INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取Method引用
     * @param clazz
     * @return
     */
    public static ExceptionMethodWrap lookup(Class clazz) {
        // 避免空指针
        if (null == clazz) {
            return null;
        }

        String exName = clazz.getName();
        ExceptionMethodWrap method = EXCEPTION_MAP.get(exName);
        if (null != method) {
            return method;
        }
        Class<?> superClass = clazz.getSuperclass();
        return lookup(superClass);
    }

    /**
     * 更新
     * @param name
     * @param method
     */
    public static void update(String name, Method method) {
        Object instance = null;
        try {
            instance = method.getDeclaringClass().newInstance();
        } catch (Exception e) {
            return;
        }

        String instanceKey = getInstanceKey(name, method);
        INSTANCE_MAP.putIfAbsent(instanceKey, instance);
        ExceptionMethodWrap wrap = new ExceptionMethodWrap(method, INSTANCE_MAP.get(instanceKey));
        EXCEPTION_MAP.put(name, wrap);
    }

    /**
     * 根据包名进行注册
     * @param prefix
     */
    public static void updateForPackage(String prefix) {
        Reflections reflections = new Reflections(prefix, new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodParameterNamesScanner());
        Set<Method> resources = reflections.getMethodsAnnotatedWith(ExceptionProcessor.class);
        resources.parallelStream().forEach(method -> {
            ExceptionProcessor exceptionProcessor = method.getAnnotation(ExceptionProcessor.class);
            Class<? extends Throwable>[] exception = exceptionProcessor.value();
            Arrays.asList(exception).parallelStream().forEach(ex -> {
                update(ex.getName(), method);
            });
        });
    }

    /**
     * 实例key
     * @param name
     * @param method
     * @return
     */
    private static String getInstanceKey(String name, Method method) {
        return String.format("%s-%s", name, method.getDeclaringClass().getName());
    }
}