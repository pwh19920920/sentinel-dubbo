package com.xmutca.sentinel.dubbo.core.result;

import java.io.Serializable;

/**
 * 回执
 * @author: 彭伟煌(pengweihuang@xmutca.com)
 * @create: 2016-07-08 23:25
 */
public class Receipt extends Result<Object> implements Serializable {

    public Receipt() {

    }

    public Receipt(Status status) {
        super(status);
    }

    public Receipt(String message) {
        super(message);
    }

    public Receipt(Status status, String message) {
        super(status, message);
    }
}