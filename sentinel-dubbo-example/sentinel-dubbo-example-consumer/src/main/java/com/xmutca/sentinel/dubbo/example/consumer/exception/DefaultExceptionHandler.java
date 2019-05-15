package com.xmutca.sentinel.dubbo.example.consumer.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.xmutca.sentinel.dubbo.core.result.Receipt;
import com.xmutca.sentinel.dubbo.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-21
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class DefaultExceptionHandler {

    /**
     * 系统异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Receipt handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        if (ex instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) ex).getUndeclaredThrowable() instanceof BlockException) {
            response.setStatus(Result.Status.TOO_MANY_REQUESTS.getCode());
            return new Receipt(Result.Status.TOO_MANY_REQUESTS, "请求次数受限");
        }

        response.setStatus(Result.Status.ERROR.getCode());
        return new Receipt(Result.Status.ERROR, "服务器开小差了，稍后再试吧");
    }
}
