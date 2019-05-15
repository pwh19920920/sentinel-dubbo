/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.api;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.config.DashboardProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleApiPublisher;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleNacosPublisher;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public class AuthorityRuleApiPublisher implements DynamicRuleApiPublisher<AuthorityRuleEntity> {

    /**
     * 接口管理器
     */
    private SentinelApiClient sentinelApiClient;

    /**
     * 客户端管理器
     */
    private AppManagement appManagement;

    public AuthorityRuleApiPublisher(AppManagement appManagement, SentinelApiClient sentinelApiClient) {
        this.appManagement = appManagement;
        this.sentinelApiClient = sentinelApiClient;
    }

    @Override
    public AppManagement getAppManagement() {
        return appManagement;
    }

    @Override
    public boolean execute(String appName, String ip, int port, List<AuthorityRuleEntity> rules) {
        return sentinelApiClient.setAuthorityRuleOfMachine(appName, ip, port, rules);
    }
}
