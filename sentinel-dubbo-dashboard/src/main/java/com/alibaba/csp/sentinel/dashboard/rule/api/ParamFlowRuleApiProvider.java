package com.alibaba.csp.sentinel.dashboard.rule.api;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleApiProvider;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
public class ParamFlowRuleApiProvider implements DynamicRuleApiProvider<ParamFlowRuleEntity> {

    /**
     * 接口管理器
     */
    private SentinelApiClient sentinelApiClient;

    /**
     * 客户端管理器
     */
    private AppManagement appManagement;

    public ParamFlowRuleApiProvider(AppManagement appManagement, SentinelApiClient sentinelApiClient) {
        this.appManagement = appManagement;
        this.sentinelApiClient = sentinelApiClient;
    }

    @Override
    public AppManagement getAppManagement() {
        return appManagement;
    }

    @Override
    public List<ParamFlowRuleEntity> execute(String appName, String ip, int port) {
        try {
            return sentinelApiClient.fetchParamFlowRulesOfMachine(appName, ip, port).get();
        } catch (Exception e) {
            // TODO
        }
        return Collections.emptyList();
    }
}
