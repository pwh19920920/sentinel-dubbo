package com.taobao.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.csp.sentinel.dashboard.rule.zookeeper.*;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
@Configuration
@EnableConfigurationProperties(DashboardProperties.class)
@ConditionalOnProperty(name = "sentinel.zookeeper.enable", havingValue = "true")
public class RuleZookeeperConfiguration implements DisposableBean, InitializingBean {

    @Autowired
    private DashboardProperties dashboardProperties;

    @Bean
    public CuratorFramework getClient() {
        if (StringUtil.isBlank(dashboardProperties.getZookeeper().getAddress())) {
            throw new IllegalArgumentException(String.format("Bad argument: serverAddr=[%s]", dashboardProperties.getZookeeper().getAddress()));
        }

        CuratorFramework client = CuratorFrameworkFactory.builder().
                connectString(dashboardProperties.getZookeeper().getAddress()).
                retryPolicy(new ExponentialBackoffRetry(dashboardProperties.getZookeeper().getSleepTimeMs(), dashboardProperties.getZookeeper().getMaxRetries())).
                build();
        client.start();
        return client;
    }

    @Bean
    public FlowRuleZookeeperProvider getFlowRuleZookeeperProvider() {
        return new FlowRuleZookeeperProvider();
    }

    @Bean
    public FlowRuleZookeeperPublisher getFlowRuleZookeeperPublisher() {
        return new FlowRuleZookeeperPublisher();
    }

    @Bean
    public AuthorityRuleZookeeperProvider getAuthorityRuleZookeeperProvider() {
        return new AuthorityRuleZookeeperProvider();
    }

    @Bean
    public AuthorityRuleZookeeperPublisher getAuthorityRuleZookeeperPublisher() {
        return new AuthorityRuleZookeeperPublisher();
    }

    @Bean
    public DegradeRuleZookeeperProvider getDegradeRuleZookeeperProvider() {
        return new DegradeRuleZookeeperProvider();
    }

    @Bean
    public DegradeRuleZookeeperPublisher getDegradeRuleZookeeperPublisher() {
        return new DegradeRuleZookeeperPublisher();
    }

    @Bean
    public ParamFlowRuleZookeeperProvider getParamFlowRuleZookeeperProvider() {
        return new ParamFlowRuleZookeeperProvider();
    }

    @Bean
    public ParamFlowRuleZookeeperPublisher getParamFlowRuleZookeeperPublisher() {
        return new ParamFlowRuleZookeeperPublisher();
    }

    @Bean
    public SystemRuleZookeeperProvider getSystemRuleZookeeperProvider() {
        return new SystemRuleZookeeperProvider();
    }

    @Bean
    public SystemRuleZookeeperPublisher getSystemRuleZookeeperPublisher() {
        return new SystemRuleZookeeperPublisher();
    }

    @Override
    public void destroy() {
        CuratorFramework client = getClient();
        if (null != client) {
            client.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(dashboardProperties.getZookeeper().getAddress(),
                String.format(FlowRuleZookeeperProvider.PATH, AppNameUtil.getAppName()),
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        ReadableDataSource<String, List<AuthorityRule>> authorityRleDataSource = new ZookeeperDataSource<>(dashboardProperties.getZookeeper().getAddress(),
                String.format(AuthorityRuleZookeeperProvider.PATH, AppNameUtil.getAppName()),
                source -> JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {}));
        AuthorityRuleManager.register2Property(authorityRleDataSource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradeRleDataSource = new ZookeeperDataSource<>(dashboardProperties.getZookeeper().getAddress(),
                String.format(DegradeRuleZookeeperProvider.PATH, AppNameUtil.getAppName()),
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
        DegradeRuleManager.register2Property(degradeRleDataSource.getProperty());

        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRleDataSource = new ZookeeperDataSource<>(dashboardProperties.getZookeeper().getAddress(),
                String.format(ParamFlowRuleZookeeperProvider.PATH, AppNameUtil.getAppName()),
                source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
        ParamFlowRuleManager.register2Property(paramFlowRleDataSource.getProperty());

        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ZookeeperDataSource<>(dashboardProperties.getZookeeper().getAddress(),
                String.format(SystemRuleZookeeperProvider.PATH, AppNameUtil.getAppName()),
                source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {}));
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
    }
}