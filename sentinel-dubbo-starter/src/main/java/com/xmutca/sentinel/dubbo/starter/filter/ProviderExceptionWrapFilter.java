package com.xmutca.sentinel.dubbo.starter.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.xmutca.sentinel.dubbo.starter.reflect.ExceptionMethodExecute;
import lombok.extern.slf4j.Slf4j;

/**
 * 提供者异常包装工具
 *
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-21
 */
@Slf4j
@Activate(group = {Constants.PROVIDER}, order = 999999)
public class ProviderExceptionWrapFilter extends AbstractDubboFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // Get origin caller.
        String application = getApplication(invocation, "");

        Entry interfaceEntry = null;
        Entry methodEntry = null;
        try {
            String resourceName = getResourceName(invoker, invocation);
            String interfaceName = invoker.getInterface().getName();
            ContextUtil.enter(resourceName, application);
            interfaceEntry = SphU.entry(interfaceName, EntryType.IN);
            methodEntry = SphU.entry(resourceName, EntryType.IN, 1, invocation.getArguments());
            return invoker.invoke(invocation);
        } catch (BlockException e) {
            // 限流情况不记录
            return new RpcResult(ExceptionMethodExecute.handlerException(e));
        } catch (Exception e) {
            Tracer.trace(e);
            return new RpcResult(ExceptionMethodExecute.handlerException(e));
        } finally {
            if (methodEntry != null) {
                methodEntry.exit(1, invocation.getArguments());
            }
            if (interfaceEntry != null) {
                interfaceEntry.exit();
            }
            ContextUtil.exit();
        }
    }
}
