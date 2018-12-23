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

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public interface DynamicRuleZookeeperPublisher<T extends RuleEntity> extends DynamicRulePublisher<T> {

    /**
     * 地址格式
     *
     * @return
     */
    String getPathFormat();

    /**
     * Zk操作
     *
     * @return
     */
    CuratorFramework getClient();

    /**
     * 规则转换器
     * @return
     */
    default Converter<List<T>, String> ruleEntityEncoder() {
        return JSON::toJSONString;
    }

    /**
     * Publish rules to remote rule configuration center for given application name.
     *
     * @param app app name
     * @param rules list of rules to push
     * @throws Exception if some error occurs
     */
    default void publish(String app, List<T> rules) throws Exception {
        String reallyPath = String.format(getPathFormat(), app);
        if (rules == null) {
            return;
        }

        Stat stat = getClient().checkExists().forPath(reallyPath);
        String rule = ruleEntityEncoder().convert(rules);
        if (stat == null) {
            getClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(reallyPath, rule.getBytes());
            return;
        }
        getClient().setData().forPath(reallyPath, rule.getBytes());
    }
}
