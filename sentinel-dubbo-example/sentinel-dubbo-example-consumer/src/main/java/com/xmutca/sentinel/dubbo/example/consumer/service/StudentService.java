package com.xmutca.sentinel.dubbo.example.consumer.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.dubbo.config.annotation.Reference;
import com.xmutca.sentinel.dubbo.example.api.facade.StudentFacade;
import com.xmutca.sentinel.dubbo.core.result.Result;
import org.springframework.stereotype.Service;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-20
 */
@Service
public class StudentService {

    @Reference(version = "1.0.0", application = "sentinel-provider", check = false, mock = "true")
    private StudentFacade studentFacade;

    /**
     * 定义资源
     * @param name
     * @return
     */
    @SentinelResource(fallback = "sayHelloFallback", blockHandler = "sayHelloBlockHandler")
    public Result<String> sayHello(String name) {
        System.out.println("consumer sayHello");
        return studentFacade.sayHello(name);
    }

    /**
     * 降级处理
     * @param name
     * @return
     */
    public Result<String> sayHelloFallback(String name) {
        System.out.println("consumer sayHelloFallback");
        return new Result<>(Result.Status.ERROR, "consumer sayHelloFallback -> " + name);
    }

    /**
     * 限流处理
     * @param name
     * @param ex
     * @return
     */
    public Result<String> sayHelloBlockHandler(String name, BlockException ex) {
        System.out.println("consumer sayHelloBlockHandler");
        return new Result<>(Result.Status.ERROR, "consumer sayHelloBlockHandler -> " + name);
    }
}
