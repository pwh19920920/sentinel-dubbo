package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.rule.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-27
 */
@Configuration
@ConditionalOnProperty(name = "sentinel.api.enable", havingValue = "true", matchIfMissing = true)
public class RuleApiConfiguration {

    /**
     * 接口管理器
     */
    @Autowired
    private SentinelApiClient sentinelApiClient;

    /**
     * 客户端管理器
     */
    @Autowired
    private AppManagement appManagement;

    @Bean
    public FlowRuleApiProvider getFlowRuleApiProvider() {
        return new FlowRuleApiProvider(appManagement, sentinelApiClient);
    }

    @Bean
    public FlowRuleApiPublisher getFlowRuleApiPublisher() {
        return new FlowRuleApiPublisher(appManagement, sentinelApiClient);
    }

    @Bean
    public AuthorityRuleApiProvider getAuthorityRuleApiProvider() {
        return new AuthorityRuleApiProvider(appManagement, sentinelApiClient);
    }

    @Bean
    public AuthorityRuleApiPublisher getAuthorityRuleApiPublisher() {
        return new AuthorityRuleApiPublisher(appManagement, sentinelApiClient);
    }

    @Bean
    public DegradeRuleApiProvider getDegradeRuleApiProvider() {
        return new DegradeRuleApiProvider(appManagement, sentinelApiClient);
    }

    @Bean
    public DegradeRuleApiPublisher getDegradeRuleApiPublisher() {
        return new DegradeRuleApiPublisher(appManagement, sentinelApiClient);
    }

    @Bean
    public ParamFlowRuleApiProvider getParamFlowRuleApiProvider() {
        return new ParamFlowRuleApiProvider(appManagement, sentinelApiClient);
    }

    @Bean
    public ParamFlowRuleApiPublisher getParamFlowRuleApiPublisher() {
        return new ParamFlowRuleApiPublisher(appManagement, sentinelApiClient);
    }

    @Bean
    public SystemRuleApiProvider getSystemRuleApiProvider() {
        return new SystemRuleApiProvider(appManagement, sentinelApiClient);
    }

    @Bean
    public SystemRuleApiPublisher getSystemRuleApiPublisher() {
        return new SystemRuleApiPublisher(appManagement, sentinelApiClient);
    }
}