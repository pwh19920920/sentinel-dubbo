package com.xmutca.sentinel.dubbo.core.exception;

import com.xmutca.sentinel.dubbo.core.result.Receipt;
import com.xmutca.sentinel.dubbo.core.result.Result;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-20
 */
public abstract class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

    /**
     * 获取状态
     * @return
     */
    public abstract Result.Status getStatus();

    /**
     * 获取回执
     * @return
     */
    public Receipt getExceptionResult() {
        return new Receipt(getStatus(), getMessage());
    }
}
