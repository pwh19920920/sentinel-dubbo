package com.taobao.csp.sentinel.dashboard.config;

import com.taobao.csp.sentinel.dashboard.rule.api.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "sentinel.zookeeper.enable", havingValue = "false", matchIfMissing = true)
public class RuleApiConfiguration {

    @Bean
    public FlowRuleApiProvider getFlowRuleApiProvider() {
        return new FlowRuleApiProvider();
    }

    @Bean
    public FlowRuleApiPublisher getFlowRuleApiPublisher() {
        return new FlowRuleApiPublisher();
    }

    @Bean
    public AuthorityRuleApiProvider getAuthorityRuleApiProvider() {
        return new AuthorityRuleApiProvider();
    }

    @Bean
    public AuthorityRuleApiPublisher getAuthorityRuleApiPublisher() {
        return new AuthorityRuleApiPublisher();
    }

    @Bean
    public DegradeRuleApiProvider getDegradeRuleApiProvider() {
        return new DegradeRuleApiProvider();
    }

    @Bean
    public DegradeRuleApiPublisher getDegradeRuleApiPublisher() {
        return new DegradeRuleApiPublisher();
    }

    @Bean
    public ParamFlowRuleApiProvider getParamFlowRuleApiProvider() {
        return new ParamFlowRuleApiProvider();
    }

    @Bean
    public ParamFlowRuleApiPublisher getParamFlowRuleApiPublisher() {
        return new ParamFlowRuleApiPublisher();
    }

    @Bean
    public SystemRuleApiProvider getSystemRuleApiProvider() {
        return new SystemRuleApiProvider();
    }

    @Bean
    public SystemRuleApiPublisher getSystemRuleApiPublisher() {
        return new SystemRuleApiPublisher();
    }
}