package com.alibaba.csp.sentinel.dashboard.rule.api;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleApiProvider;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
public class FlowRuleApiProvider implements DynamicRuleApiProvider<FlowRuleEntity> {

    /**
     * 接口管理器
     */
    private SentinelApiClient sentinelApiClient;

    /**
     * 客户端管理器
     */
    private AppManagement appManagement;

    public FlowRuleApiProvider(AppManagement appManagement, SentinelApiClient sentinelApiClient) {
        this.appManagement = appManagement;
        this.sentinelApiClient = sentinelApiClient;
    }

    @Override
    public AppManagement getAppManagement() {
        return appManagement;
    }

    @Override
    public List<FlowRuleEntity> execute(String appName, String ip, int port) {
        return sentinelApiClient.fetchFlowRuleOfMachine(appName, ip, port);
    }
}
