package com.xmutca.sentinel.dubbo.starter.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.nacos.client.utils.AppNameUtils;
import com.xmutca.sentinel.dubbo.core.exception.BaseException;
import com.xmutca.sentinel.dubbo.starter.reflect.ExceptionMethodExecute;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者异常包装
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-21
 */
@Slf4j
@Activate(group = {Constants.CONSUMER}, order = -999999)
public class CustomerExceptionWrapFilter extends AbstractDubboFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // set origin caller.
        String application = invoker.getUrl().getParameter(Constants.APPLICATION_KEY);
        if (application != null) {
            RpcContext.getContext().setAttachment(DUBBO_APPLICATION_KEY, application);
        }

        Entry interfaceEntry = null;
        Entry methodEntry = null;
        try {
            String resourceName = getResourceName(invoker, invocation);
            ContextUtil.enter(resourceName, application);

            interfaceEntry = SphU.entry(invoker.getInterface().getName(), EntryType.OUT);
            methodEntry = SphU.entry(resourceName, EntryType.OUT);

            Result result = invoker.invoke(invocation);
            Object valObj = result.getValue();

            // 判断是否需要抛异常
            if (valObj instanceof com.xmutca.sentinel.dubbo.core.result.Result) {
                com.xmutca.sentinel.dubbo.core.result.Result receipt = (com.xmutca.sentinel.dubbo.core.result.Result) valObj;
                if (receipt.getStatusEnum() == com.xmutca.sentinel.dubbo.core.result.Result.Status.TOO_MANY_REQUESTS) {
                    throw new FlowException(AppNameUtils.getAppName());
                }

                if (receipt.getStatus() >= com.xmutca.sentinel.dubbo.core.result.Result.Status.ERROR.getCode()) {
                    throw BaseException.getInstance(receipt);
                }
            }
            return result;
        } catch (BlockException e) {
            // 限流情况不记录
            return new RpcResult(ExceptionMethodExecute.handlerException(e));
        } catch (Exception e) {
            Tracer.trace(e);
            return new RpcResult(ExceptionMethodExecute.handlerException(e));
        } finally {
            if (methodEntry != null) {
                methodEntry.exit();
            }
            if (interfaceEntry != null) {
                interfaceEntry.exit();
            }
            ContextUtil.exit();
        }
    }
}
