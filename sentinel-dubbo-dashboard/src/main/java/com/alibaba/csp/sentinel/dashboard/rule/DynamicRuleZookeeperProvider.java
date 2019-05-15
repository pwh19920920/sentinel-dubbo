package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;

import java.util.Collections;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
public interface DynamicRuleZookeeperProvider<T extends RuleEntity> extends DynamicRuleProvider<T> {

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
     * 获取反射
     * @return
     */
    Class<T> getTClass();

    /**
     * 取转换器
     * @return
     */
    default Converter<String, List<T>> getRuleEntityDecoders() {
        return s -> JSON.parseArray(s, getTClass());
    }

    /**
     * 获取规则
     * @param appName
     * @return
     * @throws Exception
     */
    @Override
    default List<T> getRules(String appName) throws Exception {
        byte[] bytes = getClient().getData().forPath(String.format(getPathFormat(), appName));
        if (null == bytes || bytes.length == 0) {
            return Collections.emptyList();
        }

        String rules = new String(bytes);
        if (StringUtil.isEmpty(rules)) {
            return Collections.emptyList();
        }

        return getRuleEntityDecoders().convert(rules);
    }
}
