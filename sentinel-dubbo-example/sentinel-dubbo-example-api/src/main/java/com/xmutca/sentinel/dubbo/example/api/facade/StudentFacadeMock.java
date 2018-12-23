package com.xmutca.sentinel.dubbo.example.api.facade;

import com.xmutca.sentinel.dubbo.core.result.Result;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-20
 */
public class StudentFacadeMock implements StudentFacade {

    @Override
    public Result<String> sayHello(String name) {
        return new Result<>(Result.Status.GATEWAY_TIMEOUT, "Mock: ->" + name);
    }
}
