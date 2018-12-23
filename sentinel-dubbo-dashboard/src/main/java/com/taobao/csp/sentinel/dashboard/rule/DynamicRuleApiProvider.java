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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public interface DynamicRuleApiProvider<T extends RuleEntity> extends DynamicRuleProvider<T> {

    /**
     * app管理器
     * @return
     */
    AppManagement getAppManagement();

    /**
     * 执行查询
     * @return
     */
    List<T> getRuleQuery(MachineInfo info);

    /**
     * 比较器
     * @return
     */
    default Comparator<MachineInfo> getComparator() {
        return (e1, e2) -> {
            if (e1.getTimestamp().before(e2.getTimestamp())) {
                return 1;
            }

            if (e1.getTimestamp().after(e2.getTimestamp())) {
                return -1;
            }

            return 0;
        };
    }

    /**
     * 获取规则
     * @param appName
     * @return
     * @throws Exception
     */
    default List<T> getRules(String appName) throws Exception {
        if (StringUtil.isBlank(appName)) {
            return Collections.emptyList();
        }

        List<MachineInfo> list = getAppManagement()
                .getDetailApp(appName)
                .getMachines()
                .parallelStream()
                .filter(MachineUtil::isMachineHealth)
                .sorted(getComparator())
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        return getRuleQuery(list.get(0));
    }
}
