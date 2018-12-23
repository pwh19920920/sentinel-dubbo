package com.xmutca.sentinel.dubbo.starter;

import com.xmutca.sentinel.dubbo.starter.config.SentinelConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-23
 */
@Configuration
@Import(SentinelConfiguration.class)
public class SentinelAutoConfiguration {
}
