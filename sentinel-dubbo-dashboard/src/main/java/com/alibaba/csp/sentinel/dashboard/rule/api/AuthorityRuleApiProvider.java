package com.alibaba.csp.sentinel.dashboard.rule.api;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.config.DashboardProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleApiProvider;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleNacosProvider;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.List;
import java.util.Optional;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
public class AuthorityRuleApiProvider implements DynamicRuleApiProvider<AuthorityRuleEntity> {

    /**
     * 接口管理器
     */
    private SentinelApiClient sentinelApiClient;

    /**
     * 客户端管理器
     */
    private AppManagement appManagement;

    public AuthorityRuleApiProvider(AppManagement appManagement, SentinelApiClient sentinelApiClient) {
        this.appManagement = appManagement;
        this.sentinelApiClient = sentinelApiClient;
    }

    @Override
    public AppManagement getAppManagement() {
        return appManagement;
    }

    @Override
    public List<AuthorityRuleEntity> execute(String appName, String ip, int port) {
        return sentinelApiClient.fetchAuthorityRulesOfMachine(appName, ip, port);
    }
}
