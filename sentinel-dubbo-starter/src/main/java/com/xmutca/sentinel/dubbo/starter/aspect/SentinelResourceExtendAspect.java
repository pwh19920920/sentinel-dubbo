package com.xmutca.sentinel.dubbo.starter.aspect;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.annotation.aspectj.AbstractSentinelAspectSupport;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.xmutca.sentinel.dubbo.core.result.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

/**
 * Aspect for methods with {@link SentinelResource} annotation.
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-20
 */
@Aspect
public class SentinelResourceExtendAspect extends AbstractSentinelAspectSupport {

    @Pointcut("@annotation(com.alibaba.csp.sentinel.annotation.SentinelResource)")
    public void sentinelResourceAnnotationPointcut() {
    }

    @Around("sentinelResourceAnnotationPointcut()")
    public Object invokeResourceWithSentinel(ProceedingJoinPoint pjp) throws Throwable {
        Method originMethod = resolveMethod(pjp);

        SentinelResource annotation = originMethod.getAnnotation(SentinelResource.class);
        if (annotation == null) {
            // Should not go through here.
            throw new IllegalStateException("Wrong state for SentinelResource annotation");
        }
        String resourceName = getResourceName(annotation.value(), originMethod);
        EntryType entryType = annotation.entryType();
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName, entryType, 1, pjp.getArgs());
            Object result = pjp.proceed();
            if (result instanceof Result && ((Result) result).getStatus() >= Result.Status.ERROR.getCode()) {
                Tracer.trace(new RuntimeException(((Result) result).getMessage()));
            }
            return result;
        } catch (BlockException ex) {
            return handleBlockException(pjp, annotation, ex);
        } catch (Throwable ex) {
            Tracer.trace(ex);
            throw ex;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
