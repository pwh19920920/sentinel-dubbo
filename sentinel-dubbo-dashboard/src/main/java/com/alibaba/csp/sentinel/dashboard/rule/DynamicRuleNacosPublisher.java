package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-27
 */
public interface DynamicRuleNacosPublisher<T extends RuleEntity> extends DynamicRulePublisher<T> {

    /**
     * 规则转换器
     * @return
     */
    default Converter<List<T>, String> ruleEntityEncoder() {
        return JSON::toJSONString;
    }

    /**
     * 获取配置服务
     * @return
     */
    ConfigService getConfigService();

    /**
     * 地址格式
     * @return
     */
    String getPathFormat();

    /**
     * 获取分组
     * @return
     */
    String getGroupId();

    /**
     * 发布
     * @param app app name
     * @param rules list of rules to push
     * @throws Exception
     */
    @Override
    default void publish(String app, List<T> rules) throws Exception {
        // 判空
        if (rules == null) {
            return;
        }

        // 发布规则
        String reallyPath = String.format(getPathFormat(), app);
        String rule = ruleEntityEncoder().convert(rules);
        getConfigService().publishConfig(reallyPath, getGroupId(), rule);
    }
}
