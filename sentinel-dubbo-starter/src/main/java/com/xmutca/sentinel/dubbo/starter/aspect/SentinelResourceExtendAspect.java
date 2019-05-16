package com.xmutca.sentinel.dubbo.starter.aspect;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.annotation.aspectj.AbstractSentinelAspectSupport;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.nacos.client.utils.AppNameUtils;
import com.xmutca.sentinel.dubbo.core.exception.BaseException;
import com.xmutca.sentinel.dubbo.core.result.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

/**
 * Aspect for methods with {@link SentinelResource} annotation.
 *
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

            // 判断是否需要抛异常
            if (result instanceof Result) {
                Result receipt = (Result) result;
                if (receipt.getStatusEnum() == Result.Status.TOO_MANY_REQUESTS) {
                    return handleBlockException(pjp, annotation, new FlowException(AppNameUtils.getAppName()));
                }

                if (receipt.getStatus() >= Result.Status.ERROR.getCode()) {
                    throw BaseException.getInstance(receipt);
                }
            }
            return result;
        } catch (BlockException ex) {
            return handleBlockException(pjp, annotation, ex);
        } catch (Throwable ex) {
            Class<? extends Throwable>[] exceptionsToIgnore = annotation.exceptionsToIgnore();
            // The ignore list will be checked first.
            if (exceptionsToIgnore.length > 0 && exceptionBelongsTo(ex, exceptionsToIgnore)) {
                throw ex;
            }

            if (exceptionBelongsTo(ex, annotation.exceptionsToTrace())) {
                traceException(ex, annotation);
                return handleFallback(pjp, annotation, ex);
            }

            // No fallback function can handle the exception, so throw it out.
            throw ex;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
