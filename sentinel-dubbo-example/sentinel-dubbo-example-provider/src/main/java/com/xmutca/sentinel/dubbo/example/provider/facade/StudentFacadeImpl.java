package com.xmutca.sentinel.dubbo.example.provider.facade;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.dubbo.config.annotation.Service;
import com.xmutca.sentinel.dubbo.example.api.facade.StudentFacade;
import com.xmutca.sentinel.dubbo.core.result.Result;

import java.util.Random;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-20
 */
@Service(
        version = "${dubbo.application.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class StudentFacadeImpl implements StudentFacade {

    private static Random random = new Random();

    @Override
    @SentinelResource(fallback = "sayHelloFallback", blockHandler = "sayHelloBlockHandler")
    public Result<String> sayHello(String name) {
        if (random.nextBoolean()) {
            throw new RuntimeException("故意抛异常");
        }
        return new Result<>("Hello World: -> " + name);
    }

    /**
     * fallback处理
     *
     * @param name
     * @return
     */
    public Result<String> sayHelloFallback(String name) {
        System.out.println("consumer sayHelloFallback");
        return new Result<>(Result.Status.ERROR, "provider sayHelloFallback -> " + name);
    }

    /**
     * 限流处理
     *
     * @param name
     * @param ex
     * @return
     */
    public Result<String> sayHelloBlockHandler(String name, BlockException ex) {
        System.out.println("consumer sayHelloBlockHandler");
        return new Result<>(Result.Status.ERROR, "provider sayHelloBlockHandler -> " + name);
    }
}
