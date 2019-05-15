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
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleApiPublisher;

import java.util.List;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public class DegradeRuleApiPublisher implements DynamicRuleApiPublisher<DegradeRuleEntity> {

    /**
     * 接口管理器
     */
    private SentinelApiClient sentinelApiClient;

    /**
     * 客户端管理器
     */
    private AppManagement appManagement;

    public DegradeRuleApiPublisher(AppManagement appManagement, SentinelApiClient sentinelApiClient) {
        this.appManagement = appManagement;
        this.sentinelApiClient = sentinelApiClient;
    }

    @Override
    public AppManagement getAppManagement() {
        return appManagement;
    }

    @Override
    public boolean execute(String appName, String ip, int port, List<DegradeRuleEntity> rules) {
        return sentinelApiClient.setDegradeRuleOfMachine(appName, ip, port, rules);
    }
}
