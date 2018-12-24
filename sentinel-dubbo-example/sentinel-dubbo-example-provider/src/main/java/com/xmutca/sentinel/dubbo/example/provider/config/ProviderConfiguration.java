package com.xmutca.sentinel.dubbo.example.provider.config;

import com.xmutca.sentinel.dubbo.starter.reflect.ExceptionRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-24
 */
@Configuration
public class ProviderConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        ExceptionRegistry.updateForPackage("com.xmutca.sentinel.dubbo.example.provider.exception");
    }
}
