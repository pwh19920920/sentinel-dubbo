package com.alibaba.csp.sentinel.dashboard.rule.zookeeper;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleZookeeperProvider;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
public class FlowRuleZookeeperProvider implements DynamicRuleZookeeperProvider<FlowRuleEntity> {

    @Autowired
    private CuratorFramework client;

    public static final String PATH = "/sentinel/rules/%s/flow";

    @Override
    public String getPathFormat() {
        return PATH;
    }

    @Override
    public CuratorFramework getClient() {
        return client;
    }

    @Override
    public Class<FlowRuleEntity> getTClass() {
        return FlowRuleEntity.class;
    }
}
