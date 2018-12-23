package com.xmutca.sentinel.dubbo.starter.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.xmutca.sentinel.dubbo.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * 提供者异常包装工具
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-21
 */
@Slf4j
@Activate(group = {Constants.PROVIDER}, order = 999999)
public class ProviderExceptionWrapFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation);
        if (result.hasException()) {
            Throwable ex = result.getException();
            if (ex instanceof ServiceException) {
                // 1. 转换异常
                ServiceException serviceException = (ServiceException) result;

                // 2. 记录日志
                log.error("ServiceException: Interface -> {}, method -> {}, status -> {}, message -> {}", invoker.getInterface().getName(), invocation.getMethodName(), serviceException.getStatus(), serviceException.getMessage(), serviceException);

                // 3. 包装异常
                return new RpcResult(serviceException.getExceptionResult());
            }

            //  其他异常
            log.error("Exception: Interface -> {}, method -> {}, exception: {}, cause: {}", invoker.getInterface().getName(), invocation.getMethodName(), ex.getClass().getName(), ex.getCause(), ex);
            return new RpcResult(new com.xmutca.sentinel.dubbo.core.result.Result<>(com.xmutca.sentinel.dubbo.core.result.Result.Status.ERROR, "系统开小差，熔断降级处理。"));
        }
        return result;
    }
}
