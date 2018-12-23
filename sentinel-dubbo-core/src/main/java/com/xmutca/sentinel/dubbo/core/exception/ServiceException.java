package com.xmutca.sentinel.dubbo.core.exception;

import com.xmutca.sentinel.dubbo.core.result.Result;
import lombok.Getter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-20
 */
public class ServiceException extends BaseException {

    /**
     * 状态
     */
    @Getter
    private Result.Status status = Result.Status.BAD_REQUEST;

    public ServiceException(String message) {
        super(message);
    }
}
