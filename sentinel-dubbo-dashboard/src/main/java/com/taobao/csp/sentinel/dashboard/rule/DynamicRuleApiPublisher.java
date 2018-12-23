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
package com.taobao.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.taobao.csp.sentinel.dashboard.discovery.AppManagement;
import com.taobao.csp.sentinel.dashboard.discovery.MachineInfo;
import com.taobao.csp.sentinel.dashboard.util.MachineUtil;

import java.util.List;
import java.util.Set;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public interface DynamicRuleApiPublisher<T extends RuleEntity> extends DynamicRulePublisher<T> {

    /**
     * app管理器
     * @return
     */
    AppManagement getAppManagement();

    /**
     * 推送
     * @param app
     * @param machine
     * @param rules
     */
    void rulePush(String app, MachineInfo machine, List<T> rules);

    /**
     * Publish rules to remote rule configuration center for given application name.
     *
     * @param app app name
     * @param rules list of rules to push
     * @throws Exception if some error occurs
     */
    default void publish(String app, List<T> rules) {
        if (StringUtil.isBlank(app)) {
            return;
        }

        if (rules == null || rules.isEmpty()) {
            return;
        }

        Set<MachineInfo> set = getAppManagement().getDetailApp(app).getMachines();

        for (MachineInfo machine : set) {
            if (!MachineUtil.isMachineHealth(machine)) {
                continue;
            }
            rulePush(app, machine, rules);
        }
    }
}
