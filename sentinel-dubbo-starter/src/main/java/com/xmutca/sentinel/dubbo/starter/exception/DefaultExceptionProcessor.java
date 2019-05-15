package com.xmutca.sentinel.dubbo.starter.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.dubbo.rpc.RpcException;
import com.xmutca.sentinel.dubbo.core.annotation.ExceptionProcessor;
import com.xmutca.sentinel.dubbo.core.exception.ServiceException;
import com.xmutca.sentinel.dubbo.core.result.Result;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-24
 */
public class DefaultExceptionProcessor {

    /**
     * BlockException
     * @param ex
     * @return
     */
    @ExceptionProcessor({BlockException.class})
    public Result handleBlockException(BlockException ex) {
        return new Result(Result.Status.TOO_MANY_REQUESTS, "请求次数受限");
    }

    /**
     * RpcException
     * @param ex
     * @return
     */
    @ExceptionProcessor({RpcException.class})
    public Result handleRpcException(RpcException ex) {
        return new Result(Result.Status.BAD_GATEWAY, "请求数据失败");
    }

    /**
     * 默认异常
     * @param ex
     * @return
     */
    @ExceptionProcessor(Exception.class)
    public Result handleException(Exception ex) {
        return Result.DEFAULT_ERROR_RESULT;
    }

    /**
     * 业务异常处理
     * @param ex
     * @return
     */
    @ExceptionProcessor({ServiceException.class})
    public Result handleServiceException(ServiceException ex) {
        return ex.getExceptionResult();
    }
}
