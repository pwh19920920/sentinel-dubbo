package com.xmutca.sentinel.dubbo.starter.aspect;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.adapter.servlet.util.FilterUtil;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.xmutca.sentinel.dubbo.core.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weihuang.peng
 */
@Aspect
public class SentinelRequestMappingAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void sentinelResourcePackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are
        // in the advices.
    }

    @Around("sentinelResourcePackagePointcut()")
    public Object sentinelResourceAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        String origin = parseOrigin(request);
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String uriTarget = StringUtils.defaultString(pattern, FilterUtil.filterTarget(request));

        Entry entry = null;
        // 务必保证finally会被执行
        try {

            // 资源名可使用任意有业务语义的字符串
            // 注意此处只是类名#方法名，方法重载是合并的，如果需要进行区分，
            // 可以获取参数类型加入到资源名称上
            UrlCleaner urlCleaner = WebCallbackManager.getUrlCleaner();
            if (urlCleaner != null) {
                uriTarget = urlCleaner.clean(uriTarget);
            }
            RecordLog.info(String.format("[Sentinel Pre Filter] Origin: %s enter Uri Path: %s", origin, uriTarget));
            entry = SphU.entry(uriTarget, EntryType.IN);

            // 被保护的业务逻辑
            return joinPoint.proceed();
        } catch (BaseException ex) {
            // 业务异常可以忽略
            throw ex;
        } catch (Exception ex) {
            // 资源访问阻止，被限流或被降级
            // 进行相应的处理操作
            // 判断是否需要被异常计数
            if (!BlockException.isBlockException(ex)) {
                Tracer.trace(ex);
            }

            // 该怎么样还是怎么样，扔给全剧异常处理
            RecordLog.warn(String.format("[Sentinel Pre Filter] Block Exception when Origin: %s enter fall back uri: %s", origin, uriTarget), ex);
            throw ex;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    /**
     * 转换目标
     *
     * @param request
     * @return
     */
    private String parseOrigin(HttpServletRequest request) {
        RequestOriginParser originParser = WebCallbackManager.getRequestOriginParser();
        String origin = EMPTY_ORIGIN;
        if (originParser != null) {
            origin = originParser.parseOrigin(request);
            if (StringUtil.isEmpty(origin)) {
                return EMPTY_ORIGIN;
            }
        }
        return origin;
    }


    private static final String EMPTY_ORIGIN = "";
}