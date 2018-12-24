package com.xmutca.sentinel.dubbo.example.provider.exception;

import com.xmutca.sentinel.dubbo.core.annotation.ExceptionProcessor;
import com.xmutca.sentinel.dubbo.core.exception.ServiceException;
import com.xmutca.sentinel.dubbo.core.result.Receipt;
import com.xmutca.sentinel.dubbo.core.result.Result;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-24
 */
public class ExceptionHandler {

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
        Receipt result = ex.getExceptionResult();
        result.setMessage("项目的实现 -》" + result.getMessage());
        return result;
    }
}
