package com.xmutca.sentinel.dubbo.example.consumer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

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
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(value = Exception.class)
//    public Receipt handleException(Exception ex, HttpServletRequest request) {
//        return new Receipt(Result.Status.ERROR, "服务器开小差了，稍后再试吧");
//    }
}
