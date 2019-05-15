package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.dashboard.rule.nacos.*;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-27
 */
@Configuration
@EnableConfigurationProperties(DashboardProperties.class)
@ConditionalOnProperty(name = "sentinel.nacos.enable", havingValue = "true")
public class RuleNacosConfiguration {

    public static final Logger LOGGER = LoggerFactory.getLogger(RuleNacosConfiguration.class);

    @Autowired
    private DashboardProperties dashboardProperties;

    @Bean
    public ConfigService getConfigService() {
        try {
            return ConfigFactory.createConfigService(dashboardProperties.getNacos().getServerAddr());
        } catch (NacosException e) {
            LOGGER.error("nacos config client start failure");
            throw new RuntimeException(e);
        }
    }

    @Bean
    public FlowRuleNacosProvider getFlowRuleNacosProvider() {
        return new FlowRuleNacosProvider(getConfigService(), dashboardProperties);
    }

    @Bean
    public FlowRuleNacosPublisher getFlowRuleNacosPublisher() {
        return new FlowRuleNacosPublisher(getConfigService(), dashboardProperties);
    }

    @Bean
    public AuthorityRuleNacosProvider getAuthorityRuleNacosProvider() {
        return new AuthorityRuleNacosProvider(getConfigService(), dashboardProperties);
    }

    @Bean
    public AuthorityRuleNacosPublisher getAuthorityRuleNacosPublisher() {
        return new AuthorityRuleNacosPublisher(getConfigService(), dashboardProperties);
    }

    @Bean
    public DegradeRuleNacosProvider getDegradeRuleNacosProvider() {
        return new DegradeRuleNacosProvider(getConfigService(), dashboardProperties);
    }

    @Bean
    public DegradeRuleNacosPublisher getDegradeRuleNacosPublisher() {
        return new DegradeRuleNacosPublisher(getConfigService(), dashboardProperties);
    }

    @Bean
    public ParamFlowRuleNacosProvider getParamFlowRuleNacosProvider() {
        return new ParamFlowRuleNacosProvider(getConfigService(), dashboardProperties);
    }

    @Bean
    public ParamFlowRuleNacosPublisher getParamFlowRuleNacosPublisher() {
        return new ParamFlowRuleNacosPublisher(getConfigService(), dashboardProperties);
    }

    @Bean
    public SystemRuleNacosProvider getSystemRuleNacosProvider() {
        return new SystemRuleNacosProvider(getConfigService(), dashboardProperties);
    }

    @Bean
    public SystemRuleNacosPublisher getSystemRuleNacosPublisher() {
        return new SystemRuleNacosPublisher(getConfigService(), dashboardProperties);
    }
}