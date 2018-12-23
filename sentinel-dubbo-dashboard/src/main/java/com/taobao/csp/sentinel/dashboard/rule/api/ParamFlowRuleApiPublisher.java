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
package com.taobao.csp.sentinel.dashboard.rule.api;

import com.taobao.csp.sentinel.dashboard.client.SentinelApiClient;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.discovery.AppManagement;
import com.taobao.csp.sentinel.dashboard.discovery.MachineInfo;
import com.taobao.csp.sentinel.dashboard.rule.DynamicRuleApiPublisher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public class ParamFlowRuleApiPublisher implements DynamicRuleApiPublisher<ParamFlowRuleEntity> {

    @Autowired
    private SentinelApiClient sentinelApiClient;
    @Autowired
    private AppManagement appManagement;

    @Override
    public AppManagement getAppManagement() {
        return appManagement;
    }

    @Override
    public void rulePush(String app, MachineInfo machine, List<ParamFlowRuleEntity> rules) {
        sentinelApiClient.setParamFlowRuleOfMachine(app, machine.getIp(), machine.getPort(), rules);

    }
}
